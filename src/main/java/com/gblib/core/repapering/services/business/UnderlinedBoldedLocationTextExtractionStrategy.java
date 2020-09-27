package com.gblib.core.repapering.services.business;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gblib.core.repapering.controller.WorkflowInitiateController;
import com.gblib.core.repapering.model.DocumentMetaData;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import com.itextpdf.text.pdf.parser.Matrix;

public class UnderlinedBoldedLocationTextExtractionStrategy extends LocationTextExtractionStrategy{

	private static final Logger LOGGER = LoggerFactory.getLogger(UnderlinedBoldedLocationTextExtractionStrategy.class);
	
    protected List<TextChunkInfo> foundItems = new ArrayList<TextChunkInfo>();
    
    private List<String> boldAndUnderlinedText;
    
    private List<TextChunkInfo> allLocations = new ArrayList<TextChunkInfo>();
    
    private List<Float> lineHeights = new ArrayList<Float>();
	    
	private List<DocumentMetaData> docMetaData = new ArrayList<DocumentMetaData>();
		    
    public UnderlinedBoldedLocationTextExtractionStrategy()
    {
        setBoldAndUnderlinedText(new ArrayList<String>());
    }
    
    public List<DocumentMetaData> getDocMetaData() {
		return docMetaData;
	}

	public List<String> getBoldAndUnderlinedText() {
		return boldAndUnderlinedText;
	}

	public void setBoldAndUnderlinedText(List<String> boldAndUnderlinedText) {
		this.boldAndUnderlinedText = boldAndUnderlinedText;
	}
	
	public void clean() {
		foundItems.clear();
		boldAndUnderlinedText.clear();
		allLocations.clear();
		lineHeights.clear();
		docMetaData.clear();
	}
    private static LineSegment getSegment(TextRenderInfo info)
    {
    	LineSegment segment = info.getBaseline();
        if (Math.abs(info.getRise()) > 0.01)
            segment.transformBy(new Matrix(0, -info.getRise()));
        return segment;
    }                    
    
    //Automatically called for each chunk of text in the PDF
    public void renderText(TextRenderInfo renderInfo)
    {
    	super.renderText(renderInfo);   	    	
    	
    	if(null != renderInfo) {
    		TextChunkInfo textChunkInfo = new TextChunkInfo(renderInfo);        
    		allLocations.add(textChunkInfo);
    		
    		
    		if (renderInfo.getFont().getPostscriptFontName().contains("Bold")) {
    			//Add this to our found collection
    			foundItems.add(new TextChunkInfo(renderInfo));    			
    			//LOGGER.info("Bold Text:- " + renderInfo.getText());
    			
    		}

    		if (!lineHeights.contains(textChunkInfo.lineHeight))
    			lineHeights.add(textChunkInfo.lineHeight);
    	}
    }
    
    //
    public String getResultantText()
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder header = new StringBuilder();
        TextChunkInfo lastFound = null;
        
        float leftEdge = (float)allLocations.stream().mapToDouble(m->m.getStartLocation().get(Vector.I1)).min().getAsDouble();
        float rightEdge = (float)allLocations.stream().mapToDouble(m->m.getEndLocation().get(Vector.I2)).max().getAsDouble();
        
        
        for (TextChunkInfo info : foundItems)
        {
            if (null == lastFound) {
                sb.append(info.getText());
            	header.append(info.getText());
            }
            else
            {
                // Combine successive words
                //  both inline            	
                boolean inline = info.getStartLocation().get(Vector.I2) == lastFound.getStartLocation().get(Vector.I2);
                             //&& Math.abs(info.distanceFromEndOf(lastFound)) < 14; //Update to 14 as value comes 13 .
                //TODO:  and split across lines
                boolean splitAcrossLines = info.getStartLocation().get(Vector.I1) - leftEdge < 2
                                       && rightEdge - lastFound.getEndLocation().get(Vector.I1) < info.getCharSpaceWidth() + info.getEndLocation().get(Vector.I1) - info.getStartLocation().get(Vector.I1)//45
                                       && onNextLine(info, lastFound);
                
                
                	if (inline || splitAcrossLines) {
                		//sb.append(" ");
                		header.append(info.getText());
                	}
                	else {
                		sb.append("\r\n");                		
                		docMetaData.add(CreateMetaDataInstance(lastFound,header));
                		header.setLength(0);//Reinitialize it.
                		header.append(info.getText());
                	}
                
                sb.append(info.getText());
                
            }

            lastFound = info;
        }        
        return sb.toString();
    }
    //
    //CreateMetaDataInstance
    private DocumentMetaData CreateMetaDataInstance(TextChunkInfo info,StringBuilder header) {
    	DocumentMetaData metadata = new DocumentMetaData();
    	metadata.setHeaderName(header.toString());
    	metadata.setStartLocationX(info.getStartLocation().get(Vector.I1));
    	metadata.setStartLocationY(info.getStartLocation().get(Vector.I2));
    	metadata.setEndLocationX(info.getEndLocation().get(Vector.I1));
    	metadata.setEndLocationY(info.getEndLocation().get(Vector.I2));    	
    	return metadata;
    }
    //
    private boolean onNextLine(TextChunkInfo firstChunk, TextChunkInfo lastChunk)
    {
        //TODO: Account for lines not being exactly at the same height
        return lineHeights.indexOf(firstChunk.lineHeight) == lineHeights.indexOf(lastChunk.lineHeight) + 1;
    }

	public List<TextChunkInfo> getFoundItems() {
		return foundItems;
	}


	public class TextChunkInfo extends TextChunk
    {
        public TextChunkInfo(TextRenderInfo info) {             
        	this(getSegment(info).getStartPoint(), getSegment(info).getEndPoint(), info);        	
            }
        
        private TextChunkInfo(Vector startLocation, Vector endLocation, TextRenderInfo info){    
        	super(info.getText(), startLocation, endLocation, info.getSingleSpaceWidth());    	
        	this.renderInfo = info;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
        }
    	private Vector startLocation;
    	private Vector endLocation;
    	private float charSpaceWidth;
    	private TextRenderInfo renderInfo;
    	private float lineHeight;
    	
    	public Vector getStartLocation() {
    		return startLocation;
    	}

    	public float getCharSpaceWidth() {
    		return charSpaceWidth;
    	}

    	public void setCharSpaceWidth(float charSpaceWidth) {
    		this.charSpaceWidth = getRenderInfo().getSingleSpaceWidth();
    	}

    	public Vector getEndLocation() {
    		return endLocation;
    	}
    	
    	public TextRenderInfo getRenderInfo() {
    		return renderInfo;
    	}
    	
    	public float getLineHeight() {    		
    		lineHeight = getSegment(renderInfo).getBoundingRectange().y +
    		getSegment(renderInfo).getBoundingRectange().height;
    		
    		return lineHeight;
    	}
    }
}
