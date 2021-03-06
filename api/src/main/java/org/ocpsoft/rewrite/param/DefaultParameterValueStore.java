/*
 * Copyright 2013 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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
package org.ocpsoft.rewrite.param;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;

/**
 * Default implementation of {@link ParameterValueStore}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class DefaultParameterValueStore implements ParameterValueStore, Iterable<Entry<Parameter<?>, String>>
{
   Map<Parameter<?>, String> map = new LinkedHashMap<Parameter<?>, String>();

   @Override
   public String retrieve(Parameter<?> parameter)
   {
      return map.get(parameter);
   }

   @Override
   public boolean submit(Rewrite event, EvaluationContext context, Parameter<?> param, String value)
   {
      boolean result = false;
      String stored = map.get(param);

      if ("*".equals(param.getName()))
      {
         result = true;
      }
      else if (stored == value || (stored != null && stored.equals(value)))
      {
         result = true;
      }
      else if (stored == null)
      {
         result = true;
         for (Constraint<String> constraint : param.getConstraints()) {
            if (!constraint.isSatisfiedBy(event, context, value))
            {
               result = false;
            }
         }
         // FIXME Transposition processing will break multi-conditional matching
         if (result)
         {
            for (Transposition<String> transposition : param.getTranspositions()) {
               value = transposition.transpose(event, context, value);
            }
            map.put(param, value);
            result = true;
         }
      }

      return result;
   }

   public String get(Parameter<?> parameter)
   {
      return map.get(parameter);
   }

   @Override
   public Iterator<Entry<Parameter<?>, String>> iterator()
   {
      return map.entrySet().iterator();
   }

   @Override
   public String toString()
   {
      return map.keySet().toString();
   }
}
