/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.clustering.jgroups.subsystem;

import static org.jboss.as.clustering.jgroups.subsystem.CommonAttributes.DEFAULT_STACK;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE_TYPE;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Access the resource bundle used to fetch local descriptions.
 * @author Paul Ferraro
 */
public class JGroupsDescriptions {

    public static final String RESOURCE_NAME = JGroupsDescriptions.class.getPackage().getName() + ".LocalDescriptions";

    private JGroupsDescriptions() {
        // Hide
    }

    static ModelNode getSubsystemDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        ModelNode subsystem = createDescription(resources, "jgroups");
        subsystem.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.CURRENT.getUri());
        // children of the root subsystem
        subsystem.get(CHILDREN, ModelKeys.STACK, DESCRIPTION).set(resources.getString("jgroups.stack"));
        subsystem.get(CHILDREN, ModelKeys.STACK, MIN_OCCURS).set(1);
        subsystem.get(CHILDREN, ModelKeys.STACK, MAX_OCCURS).set(Integer.MAX_VALUE);
        subsystem.get(CHILDREN, ModelKeys.STACK, MODEL_DESCRIPTION).setEmptyObject();
        return subsystem;
    }

    static ModelNode getSubsystemAddDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        final ModelNode op = createOperationDescription(ADD, resources, "jgroups.add");
        DEFAULT_STACK.addOperationParameterDescription(resources, "jgroups", op);
        return op;
    }

    static ModelNode getSubsystemRemoveDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        final ModelNode op = createOperationDescription(REMOVE, resources, "jgroups.remove");
        op.get(REPLY_PROPERTIES).setEmptyObject();
        op.get(REQUEST_PROPERTIES).setEmptyObject();
        return op;
    }

    static ModelNode getSubsystemDescribeDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        final ModelNode op = createOperationDescription(DESCRIBE, resources, "jgroups.describe");
        op.get(REQUEST_PROPERTIES).setEmptyObject();
        op.get(REPLY_PROPERTIES, TYPE).set(ModelType.LIST);
        op.get(REPLY_PROPERTIES, VALUE_TYPE).set(ModelType.OBJECT);
        return op;
    }

    static ModelNode getProtocolStackDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        final ModelNode stack = createDescription(resources, "jgroups.stack");
        // attributes
        for (AttributeDefinition attr : CommonAttributes.STACK_ATTRIBUTES) {
            attr.addResourceAttributeDescription(resources, "jgroups.stack", stack);
        }

        // information about its child "transport=TRANSPORT"
        stack.get(CHILDREN, ModelKeys.TRANSPORT, DESCRIPTION).set(resources.getString("jgroups.stack.transport"));
        stack.get(CHILDREN, ModelKeys.TRANSPORT, MIN_OCCURS).set(0);
        stack.get(CHILDREN, ModelKeys.TRANSPORT, MAX_OCCURS).set(1);
        stack.get(CHILDREN, ModelKeys.TRANSPORT, ALLOWED).setEmptyList().add(ModelKeys.TRANSPORT_NAME);
        stack.get(CHILDREN, ModelKeys.TRANSPORT, MODEL_DESCRIPTION);

        // information about its child "protocol=*"
        stack.get(CHILDREN, ModelKeys.PROTOCOL, DESCRIPTION).set(resources.getString("jgroups.stack.protocol"));
        stack.get(CHILDREN, ModelKeys.PROTOCOL, MIN_OCCURS).set(0);
        stack.get(CHILDREN, ModelKeys.PROTOCOL, MAX_OCCURS).set(Integer.MAX_VALUE);
        stack.get(CHILDREN, ModelKeys.PROTOCOL, MODEL_DESCRIPTION);

        return stack ;
    }

    static ModelNode getProtocolStackAddDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);

        final ModelNode op = createOperationDescription(ADD, resources, "jgroups.stack.add");
        // request parameters
        for (AttributeDefinition attr : CommonAttributes.STACK_ATTRIBUTES) {
            attr.addOperationParameterDescription(resources, "jgroups.stack", op);
        }
        return op;
    }

    static ModelNode getProtocolStackRemoveDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        final ModelNode op = createOperationDescription(REMOVE, resources, "jgroups.stack.remove");
        op.get(REQUEST_PROPERTIES).setEmptyObject();
        return op;
    }

    private static ResourceBundle getResources(Locale locale) {
        return ResourceBundle.getBundle(RESOURCE_NAME, (locale == null) ? Locale.getDefault() : locale);
    }

    private static ModelNode createDescription(ResourceBundle resources, String key) {
        return createOperationDescription(null, resources, key);
    }

    private static ModelNode createOperationDescription(String operation, ResourceBundle resources, String key) {
        ModelNode description = new ModelNode();
        if (operation != null) {
            description.get(ModelDescriptionConstants.OPERATION_NAME).set(operation);
        }
        description.get(ModelDescriptionConstants.DESCRIPTION).set(resources.getString(key));
        return description;
    }

    private static ModelNode createSubsystemOperationDescription(String operation, ResourceBundle resources) {
        return createOperationDescription(operation, resources, "jgroups." + operation);
    }

    private static ModelNode createProtocolStackOperationDescription(String operation, ResourceBundle resources) {
        return createOperationDescription(operation, resources, "jgroups.stack." + operation);
    }
}
