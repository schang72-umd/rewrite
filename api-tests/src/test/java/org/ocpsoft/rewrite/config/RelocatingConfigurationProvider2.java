package org.ocpsoft.rewrite.config;

import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.SendStatus;


/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class RelocatingConfigurationProvider2 implements ConfigurationProvider<Object>
{

   public int priority()
   {
      return 5;
   }

   @Override
   public boolean handles(final Object payload)
   {
      return true;
   }

   @Override
   public Configuration getConfiguration(final Object context)
   {
      return ConfigurationBuilder.begin()
               .defineRule()
               .when(Path.matches("/priority"))
               .perform(SendStatus.code(202))
               
               .defineRule()
               .when(Path.matches("/priority2"))
               .perform(SendStatus.code(202))
               .withPriority(-1)
               
               .defineRule()
               .when(Path.matches("/priority3"))
               .perform(SendStatus.code(202))
               .withPriority(12)
               
               .defineRule()
               .when(Path.matches("/priority4"))
               .perform(SendStatus.code(202))
               .withPriority(-1)
               ;
   }

}
