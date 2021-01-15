package com.salk.json.schme.extend;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Set;

/**
 * <p/>
 * Description
 * <p/>
 *
 * @author salkli
 * @date 2021/1/14
 */
public class DraftV4QpaasPathDependenciesSyntaxChecker extends QpaasPathDependenciesSyntaxChecker {
    @Override
    protected void checkDependency(ProcessingReport report, MessageBundle bundle, String name, SchemaTree tree)
        throws ProcessingException {
        final JsonNode node = getNode(tree).get(name);
        NodeType type;

        type = NodeType.getNodeType(node);

        if (type != NodeType.ARRAY) {
            report.error(newMsg(tree, bundle, "common.dependencies.value.incorrectType").putArgument("property", name)
                .putArgument("expected", dependencyTypes).putArgument("found", type));
            return;
        }

        final int size = node.size();

        if (size == 0) {
            report.error(newMsg(tree, bundle, "common.array.empty").put("property", name));
            return;
        }

        final Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        JsonNode element;
        boolean uniqueElements = true;

        for (int index = 0; index < size; index++) {
            element = node.get(index);
            type = NodeType.getNodeType(element);
            uniqueElements = set.add(EQUIVALENCE.wrap(element));
            if (type == NodeType.STRING) {
                continue;
            }
            report.error(newMsg(tree, bundle, "common.array.element.incorrectType").put("property", name)
                .putArgument("index", index).putArgument("expected", EnumSet.of(NodeType.STRING))
                .putArgument("found", type));
        }

        if (!uniqueElements) {
            report.error(newMsg(tree, bundle, "common.array.duplicateElements").put("property", name));
        }

    }
}
