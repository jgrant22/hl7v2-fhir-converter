/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.linuxforhealth.hl7.segments.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Patient;

import io.github.linuxforhealth.fhir.FHIRContext;
import io.github.linuxforhealth.hl7.ConverterOptions;
import io.github.linuxforhealth.hl7.ConverterOptions.Builder;
import io.github.linuxforhealth.hl7.HL7ToFHIRConverter;

public class PatientUtils  {

  private static FHIRContext context = new FHIRContext();
  private static final ConverterOptions OPTIONS =
    new Builder().withValidateResource().withPrettyPrint().build();

  public static Patient createPatientFromHl7Segment(String inputSegment){
    HL7ToFHIRConverter ftv = new HL7ToFHIRConverter();
    String json = ftv.convert(inputSegment, OPTIONS);

    assertThat(json).isNotBlank();
    FHIRContext context = new FHIRContext();
    IBaseResource bundleResource = context.getParser().parseResource(json);
    assertThat(bundleResource).isNotNull();
    Bundle b = (Bundle) bundleResource;

    List<BundleEntryComponent> e = b.getEntry();
    List<Resource> patients =
        e.stream().filter(v -> ResourceType.Patient == v.getResource().getResourceType())
            .map(BundleEntryComponent::getResource).collect(Collectors.toList());
    assertThat(patients).hasSize(1);
    return getPatientFromResource(patients.get(0));
  }  

  private static Patient getPatientFromResource(Resource resource) {
    String s = context.getParser().encodeResourceToString(resource);
    Class<? extends IBaseResource> klass = Patient.class;
    return (Patient) context.getParser().parseResource(klass, s);
  }


}
