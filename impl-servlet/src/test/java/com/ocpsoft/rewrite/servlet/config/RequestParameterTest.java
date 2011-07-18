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
package com.ocpsoft.rewrite.servlet.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ocpsoft.rewrite.event.Rewrite;
import com.ocpsoft.rewrite.mock.MockRewrite;
import com.ocpsoft.rewrite.servlet.http.impl.HttpInboundRewriteImpl;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class RequestParameterTest
{
   private Rewrite rewrite;
   private HttpServletRequest request;

   @Before
   public void before()
   {
      request = EasyMock.createNiceMock(HttpServletRequest.class);
      EasyMock.expect(request.getParameterNames())
               .andReturn(Collections.enumeration(Arrays.asList("foo", "baz"))).anyTimes();

      EasyMock.expect(request.getParameterValues("foo"))
               .andReturn(new String[] { "bar" }).anyTimes();

      EasyMock.expect(request.getParameterValues("baz"))
               .andReturn(new String[] { "cab" }).anyTimes();

      EasyMock.expect(request.getParameter("foo"))
               .andReturn("bar").anyTimes();

      EasyMock.expect(request.getParameter("baz"))
               .andReturn("cab").anyTimes();

      EasyMock.replay(request);

      rewrite = new HttpInboundRewriteImpl(request, null);
   }

   @Test
   public void testRequestParameterExists()
   {
      Assert.assertTrue(RequestParameter.exists("foo").evaluate(rewrite));
   }

   @Test
   public void testRequestParameterExists2()
   {
      Assert.assertTrue(RequestParameter.exists("baz").evaluate(rewrite));
   }

   @Test
   public void testRequestParameterExistsFalse()
   {
      Assert.assertFalse(RequestParameter.exists("nope").evaluate(rewrite));
   }

   @Test
   public void testRequestParameterContains()
   {
      Assert.assertTrue(RequestParameter.valueExists("bar").evaluate(rewrite));
   }

   @Test
   public void testRequestParameterMatches()
   {
      Assert.assertTrue(RequestParameter.matches("foo", "(bar|baz)").evaluate(rewrite));
   }

   @Test(expected = PatternSyntaxException.class)
   public void testBadRegexThrowsException()
   {
      RequestParameter.matches("*whee", "blah");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullNameInput()
   {
      RequestParameter.exists(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullValueExistsInput()
   {
      RequestParameter.valueExists(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullInputs()
   {
      RequestParameter.matches(null, null);
   }

   @Test
   public void testDoesNotMatchNonHttpRewrites()
   {
      Assert.assertFalse(RequestParameter.exists("foo").evaluate(new MockRewrite()));
   }
}