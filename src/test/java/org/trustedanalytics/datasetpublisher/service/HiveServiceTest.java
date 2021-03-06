/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.datasetpublisher.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.trustedanalytics.datasetpublisher.entity.HiveTable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HiveServiceTest.HiveServiceTestConfiguration.class})
public class HiveServiceTest {

    @Autowired
    private QueryBuilder queryBuilder;

    @Autowired
    private JdbcOperations jdbcOperations;

    @Test
    public void testCreateTable() {
        // given
        final HiveService hiveService = new HiveService(jdbcOperations, queryBuilder);
        final HiveTable hiveTable = new HiveTable("db", "table", Collections.emptyList(), "loc");

        // when
        hiveService.createTable(hiveTable);

        // then
        verify(queryBuilder).createDatabase(hiveTable);
        verify(queryBuilder).createTable(hiveTable);
    }

    @Test
    public void testDropTable() {
        // given
        final HiveService hiveService = new HiveService(jdbcOperations, queryBuilder);
        final HiveTable hiveTable = new HiveTable("db", "table", Collections.emptyList(), "loc");

        // when
        hiveService.dropTable(hiveTable);

        // then
        verify(queryBuilder).dropTable(hiveTable);
    }

    @Configuration
    static class HiveServiceTestConfiguration {
        @Bean
        public QueryBuilder queryBuilder() throws SQLException {
            QueryBuilder builder = mock(QueryBuilder.class);
            when(builder.createDatabase(any())).thenReturn("sql");
            when(builder.createTable(any())).thenReturn("sql");
            when(builder.dropTable(any())).thenReturn("sql");
            return builder;
        }

        @Bean
        public JdbcOperations jdbcOperations() {
            return mock(JdbcOperations.class);
        }
    }

}
