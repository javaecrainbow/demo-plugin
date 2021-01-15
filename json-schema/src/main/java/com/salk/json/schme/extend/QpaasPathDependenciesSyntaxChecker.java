/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.salk.json.schme.extend;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonNumEquivalence;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.core.exceptions.InvalidSchemaException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * Helper class for syntax checking of draft v4 and v3 {@code dependencies}
 *
 * <p>The validation check also fills the JSON Pointer list with the
 * appropriate paths when schema dependencies are encountered.</p>
 */
public abstract class QpaasPathDependenciesSyntaxChecker
    extends AbstractSyntaxChecker
{
    /**
     * JSON Schema equivalence
     */
    protected static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquivalence.getInstance();

    /**
     * Valid types for one dependency value
     */
    protected final EnumSet<NodeType> dependencyTypes;

    /**
     * Protected constructor
     *
     * @param depTypes valid types for one dependency value
     */
    protected QpaasPathDependenciesSyntaxChecker(final NodeType... depTypes)
    {
        super("path_dependencies", NodeType.OBJECT);
        dependencyTypes = EnumSet.of(NodeType.OBJECT, depTypes);
    }

    @Override
    protected final void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = getNode(tree);
        final Map<String, JsonNode> map = Maps.newTreeMap();
        map.putAll(JacksonUtils.asMap(node));

        String key;
        JsonNode value;

        for (final Map.Entry<String, JsonNode> entry: map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value.isObject())
                pointers.add(JsonPointer.of(keyword, key));
            else
                checkDependency(report, bundle, entry.getKey(), tree);
        }

    }

    /**
     * Check one dependency which is not a schema dependency
     *
     * @param report the processing report to use
     * @param bundle the message bundle to use
     * @param name the property dependency name
     * @param tree the schema
     * @throws InvalidSchemaException keyword is invalid
     */
    protected abstract void checkDependency(final ProcessingReport report,
        final MessageBundle bundle, final String name, final SchemaTree tree)
        throws ProcessingException;
}
