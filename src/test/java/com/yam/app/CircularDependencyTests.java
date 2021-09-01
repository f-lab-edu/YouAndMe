package com.yam.app;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Disabled;

@AnalyzeClasses(packagesOf = YouAndMeApplication.class)
@Disabled
final class CircularDependencyTests {

    @ArchTest
    ArchRule cycleCheck = slices().matching("com.yam.app.(*)..")
        .should().beFreeOfCycles();
}
