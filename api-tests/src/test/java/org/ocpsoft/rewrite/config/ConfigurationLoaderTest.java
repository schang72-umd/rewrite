/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.rewrite.config;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.ocpsoft.rewrite.Root;
import org.ocpsoft.rewrite.test.HttpAction;
import org.ocpsoft.rewrite.test.RewriteTestBase;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ConfigurationLoaderTest extends RewriteTestBase
{

   @Deployment(testable = true)
   public static WebArchive getDeployment()
   {
      WebArchive deployment = RewriteTestBase.getDeployment()
               .addPackages(true, Root.class.getPackage())
               .addAsResource(new StringAsset("org.ocpsoft.rewrite.config.NullValueConfigurationProvider"),
                        "/META-INF/services/org.ocpsoft.rewrite.config.ConfigurationProvider");
      return deployment;
   }

   @Test
   public void test()
   {
      HttpAction<HttpGet> action = get("/");
      Assert.assertEquals(200, action.getResponse().getStatusLine().getStatusCode());
   }

}
