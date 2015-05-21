package org.tnel.meau.participants;

import com.wordnik.swagger.annotations.ApiModel;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "participant")
@XmlSeeAlso({Buyer.class, Seller.class})
@ApiModel(value = "Participant", description = "Participant model")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Participant {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlTransient
    protected AgentController agentController;

    public Participant() {
    }

    ;

    public Participant(String name, ContainerController containerController, Class agentClass) throws StaleProxyException {
        this.name = name;
        this.agentController = containerController.createNewAgent(name, agentClass.getName(),
                new Object[]{});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
