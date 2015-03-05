package com.github.avarabyeu.jashing.integration.jenkins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Job List Representation
 *
 * @author Andrei Varabyeu
 */
@XmlRootElement(name = "names")
@XmlAccessorType(XmlAccessType.FIELD)
public class Jobs {

    @XmlElement(name = "name")
    private List<String> names = new LinkedList<>();


    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "Jobs{" +
                "names=" + names +
                '}';
    }
}
