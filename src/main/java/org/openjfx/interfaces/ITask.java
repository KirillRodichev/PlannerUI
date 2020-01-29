package org.openjfx.interfaces;

;

import org.openjfx.enums.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

public interface ITask{
    public void finish();

    public void setName(String name);

    public void setDescription(String description);

    public void setStartDate(Date startDate);

    public void setFinishDate(Date finishDate);

    public void setType(TaskType taskType);

    public void setState(TaskState taskState);

    public void setTag(String tag);

    public void setId(int id);

    public String getName();

    public String getDescription();

    public TaskState getState();

    public Date getFinishDate();

    public Date getStartDate();

    public String getTag();

    public TaskType getType();

    public int getId();

    public void writeFormat(PrintWriter writer);

    public String toString();

    public Object clone() throws CloneNotSupportedException;

    public boolean equals(Object o);

    public void writeXML(Document document, Element root, Transformer transformer, DOMSource domSource, StreamResult streamResult) throws TransformerException;

    public void readXML(Document document) throws ParseException;
}
