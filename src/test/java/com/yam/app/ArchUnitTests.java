package com.yam.app;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = YouAndMeApplication.class)
final class ArchUnitTests {

    @ArchTest
    ArchRule cycleCheck = slices().matching("com.yam.app.(*)..")
        .should().beFreeOfCycles();

    @ArchTest
    ArchRule layerCheck = layeredArchitecture()
        .layer("Application").definedBy("..application..")
        .layer("Domain").definedBy("..domain..")
        .layer("Presentation").definedBy("..presentation..")
        .layer("Infrastructure").definedBy("..infrastructure..")
        .layer("Integration").definedBy("..integration..")

        .whereLayer("Presentation").mayOnlyBeAccessedByLayers("Application", "Integration")
        .whereLayer("Application").mayOnlyBeAccessedByLayers("Presentation", "Domain")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
        .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
        .whereLayer("Integration").mayNotBeAccessedByAnyLayer();
}
