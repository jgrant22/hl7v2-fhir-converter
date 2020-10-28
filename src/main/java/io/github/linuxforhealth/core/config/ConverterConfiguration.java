/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.linuxforhealth.core.config;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.lang3.StringUtils;

public class ConverterConfiguration {

  private static final String SUPPORTED_HL7_MESSAGES = "supported.hl7.messages";

  private static final String BASE_PATH_RESOURCE = "base.path.resource";

  private static final String CONFIG_PROPERTIES = "config.properties";

  private static ConverterConfiguration configuration;

  private String resourceFolder;
  private boolean resourcefromClassPath;
  private List<Object> supportedMessageTemplates;

  private ConverterConfiguration() {
    try {
      
      List<FileLocationStrategy> subs =
          Arrays.asList(new ConfigDirectoryLocationStrategy(),
              new ClasspathLocationStrategy());
      FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

      Parameters params = new Parameters();

      FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
          new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)

              .configure(
                  params.properties().setFileName(CONFIG_PROPERTIES)
                      .setThrowExceptionOnMissing(true).setLocationStrategy(strategy)
                  .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
      Configuration config = builder.getConfiguration();

      String resourceLoc = config.getString(BASE_PATH_RESOURCE);
      if (StringUtils.isNotBlank(resourceLoc)) {
        resourceFolder = resourceLoc;
      } else {
        resourceFolder = "";
        resourcefromClassPath = true;
      }


      supportedMessageTemplates = config.getList(SUPPORTED_HL7_MESSAGES);

    } catch (ConfigurationException e) {
      throw new IllegalStateException("Cannot read configuration for resource location", e);
    }
  }


  public static ConverterConfiguration getInstance() {
    if (configuration == null) {
      configuration = new ConverterConfiguration();
    }
    return configuration;
  }


  public String getResourceFolder() {
    return resourceFolder;
  }


  public boolean isResourcefromClassPath() {
    return resourcefromClassPath;
  }


  public void setResourcefromClassPath(boolean resourcefromClassPath) {
    this.resourcefromClassPath = resourcefromClassPath;
  }


  public List<Object> getSupportedMessageTemplates() {
    return supportedMessageTemplates;
  }




}