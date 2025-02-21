// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.parser.trino;

import org.apache.doris.nereids.parser.NereidsParser;
import org.apache.doris.nereids.parser.ParserTestBase;
import org.apache.doris.nereids.trees.plans.logical.LogicalPlan;

import org.junit.jupiter.api.Test;

/**
 * Trino to Doris function mapping test.
 */
public class FnTransformTest extends ParserTestBase {

    @Test
    public void testStringFnTransform() {
        String sql = "SELECT ascii('a') as b FROM t";
        NereidsParser nereidsParser = new NereidsParser();
        LogicalPlan logicalPlan = nereidsParser.parseSingle(sql);

        String dialectSql = "SELECT codepoint('a') as b FROM t";
        trinoDialectParsePlan(dialectSql).assertEquals(logicalPlan);
    }

    @Test
    public void testDateDiffFnTransform() {
        String dialectSql = "SELECT date_diff('second', TIMESTAMP '2020-12-25 22:00:00', TIMESTAMP '2020-12-25 21:00:00')";
        trinoDialectParsePlan(dialectSql).assertContains("seconds_diff(2020-12-25 22:00:00, 2020-12-25 21:00:00)");
    }
}
