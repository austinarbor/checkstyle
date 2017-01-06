///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2022 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.coding;

import static com.google.common.truth.Truth.assertWithMessage;
import static com.puppycrawl.tools.checkstyle.checks.coding.OverloadMethodsDeclarationOrderCheck.DEFAULT_OVERLOAD_GROUP;
import static com.puppycrawl.tools.checkstyle.checks.coding.OverloadMethodsDeclarationOrderCheck.MSG_KEY;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.checks.coding.OverloadMethodsDeclarationOrderCheck.OverloadDescriptor;
import com.puppycrawl.tools.checkstyle.checks.coding.OverloadMethodsDeclarationOrderCheck.RegexOverloadGroup;

public class OverloadMethodsDeclarationOrderCheckTest
    extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/coding/overloadmethodsdeclarationorder";
    }

    @Test
    public void testDefault() throws Exception {

        final String[] expected = {
            "32:5: " + getCheckMessage(MSG_KEY, 21),
            "60:9: " + getCheckMessage(MSG_KEY, 49),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "115:5: " + getCheckMessage(MSG_KEY, 104),
        };
        verifyWithInlineConfigParser(
                getPath("InputOverloadMethodsDeclarationOrder.java"), expected);
    }

    @Test
    public void testOverloadMethodsDeclarationOrderRecords() throws Exception {

        final String[] expected = {
            "21:9: " + getCheckMessage(MSG_KEY, 15),
            "41:9: " + getCheckMessage(MSG_KEY, 35),
            "57:9: " + getCheckMessage(MSG_KEY, 50),
        };
        verifyWithInlineConfigParser(
                getNonCompilablePath("InputOverloadMethodsDeclarationOrderRecords.java"),
            expected);
    }

    @Test
    public void testBackwardsCompatibilityCatchAllGroup() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);

        checkConfig.addProperty("modifierGroups", ".*");

        final String[] expected = {
            "32:5: " + getCheckMessage(MSG_KEY, 21),
            "60:9: " + getCheckMessage(MSG_KEY, 49),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "115:5: " + getCheckMessage(MSG_KEY, 104),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestBackwardCompatibilityDefaultGroup() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "static");
        final String[] expected = {
            "32:5: " + getCheckMessage(MSG_KEY, 21),
            "60:9: " + getCheckMessage(MSG_KEY, 49),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "115:5: " + getCheckMessage(MSG_KEY, 104),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupDuplication() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "static");
        checkConfig.addProperty("modifierGroups", "static");
        final String[] expected = {
            "32:5: " + getCheckMessage(MSG_KEY, 21),
            "60:9: " + getCheckMessage(MSG_KEY, 49),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "115:5: " + getCheckMessage(MSG_KEY, 104),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestDefaultGroupDuplication() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", ".*");
        checkConfig.addProperty("modifierGroups", ".*");
        final String[] expected = {
            "32:5: " + getCheckMessage(MSG_KEY, 21),
            "60:9: " + getCheckMessage(MSG_KEY, 49),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "115:5: " + getCheckMessage(MSG_KEY, 104),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGrouping1Static() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "static");
        final String[] expected = {
            "22:5: " + getCheckMessage(MSG_KEY, 18),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "50:5: " + getCheckMessage(MSG_KEY, 48),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "68:5: " + getCheckMessage(MSG_KEY, 64),
            "81:5: " + getCheckMessage(MSG_KEY, 77),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingPrivate() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "private");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "31:5: " + getCheckMessage(MSG_KEY, 27),
            "40:5: " + getCheckMessage(MSG_KEY, 36),
            "49:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "70:5: " + getCheckMessage(MSG_KEY, 64),
            "71:5: " + getCheckMessage(MSG_KEY, 69),
            "83:5: " + getCheckMessage(MSG_KEY, 77),
            "84:5: " + getCheckMessage(MSG_KEY, 82),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingPublic() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "public");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "31:5: " + getCheckMessage(MSG_KEY, 27),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "40:5: " + getCheckMessage(MSG_KEY, 38),
            "49:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "66:5: " + getCheckMessage(MSG_KEY, 64),
            "70:5: " + getCheckMessage(MSG_KEY, 68),
            "71:5: " + getCheckMessage(MSG_KEY, 69),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "79:5: " + getCheckMessage(MSG_KEY, 77),
            "83:5: " + getCheckMessage(MSG_KEY, 81),
            "84:5: " + getCheckMessage(MSG_KEY, 82),
            "85:5: " + getCheckMessage(MSG_KEY, 83),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingProtected() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "protected");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "31:5: " + getCheckMessage(MSG_KEY, 29),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "66:5: " + getCheckMessage(MSG_KEY, 64),
            "79:5: " + getCheckMessage(MSG_KEY, 77),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingPackage() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "package");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "31:5: " + getCheckMessage(MSG_KEY, 29),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "40:5: " + getCheckMessage(MSG_KEY, 38),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "59:5: " + getCheckMessage(MSG_KEY, 57),
            "69:5: " + getCheckMessage(MSG_KEY, 64),
            "72:5: " + getCheckMessage(MSG_KEY, 68),
            "82:5: " + getCheckMessage(MSG_KEY, 77),
            "85:5: " + getCheckMessage(MSG_KEY, 81),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingFinal() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "final");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "31:5: " + getCheckMessage(MSG_KEY, 29),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "66:5: " + getCheckMessage(MSG_KEY, 64),
            "70:5: " + getCheckMessage(MSG_KEY, 66),
            "71:5: " + getCheckMessage(MSG_KEY, 69),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "79:5: " + getCheckMessage(MSG_KEY, 77),
            "82:5: " + getCheckMessage(MSG_KEY, 79),
            "84:5: " + getCheckMessage(MSG_KEY, 81),
            "85:5: " + getCheckMessage(MSG_KEY, 83),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingSynchronizedNotExistInFile()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addProperty("modifierGroups", "synchronized");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "31:5: " + getCheckMessage(MSG_KEY, 29),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "40:5: " + getCheckMessage(MSG_KEY, 38),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "66:5: " + getCheckMessage(MSG_KEY, 64),
            "79:5: " + getCheckMessage(MSG_KEY, 77),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingPublicStatic() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addAttribute("modifierGroups", "public static");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "31:5: " + getCheckMessage(MSG_KEY, 29),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "40:5: " + getCheckMessage(MSG_KEY, 38),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "66:5: " + getCheckMessage(MSG_KEY, 64),
            "79:5: " + getCheckMessage(MSG_KEY, 77),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingStaticPublic() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addAttribute("modifierGroups", "static public");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "29:5: " + getCheckMessage(MSG_KEY, 27),
            "30:5: " + getCheckMessage(MSG_KEY, 28),
            "38:5: " + getCheckMessage(MSG_KEY, 36),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "40:5: " + getCheckMessage(MSG_KEY, 38),
            "47:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "66:5: " + getCheckMessage(MSG_KEY, 64),
            "79:5: " + getCheckMessage(MSG_KEY, 77),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGrouping1Protected2Private3Package() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addAttribute("modifierGroups", "protected, private, package");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "31:5: " + getCheckMessage(MSG_KEY, 27),
            "49:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "70:5: " + getCheckMessage(MSG_KEY, 64),
            "71:5: " + getCheckMessage(MSG_KEY, 69),
            "72:5: " + getCheckMessage(MSG_KEY, 68),
            "83:5: " + getCheckMessage(MSG_KEY, 77),
            "84:5: " + getCheckMessage(MSG_KEY, 82),
            "85:5: " + getCheckMessage(MSG_KEY, 81),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGrouping1Public2PublicFinal3Static() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        checkConfig.addAttribute("modifierGroups", "public, public final, static");
        final String[] expected = {
            "22:5: " + getCheckMessage(MSG_KEY, 18),
            "31:5: " + getCheckMessage(MSG_KEY, 27),
            "39:5: " + getCheckMessage(MSG_KEY, 37),
            "50:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "68:5: " + getCheckMessage(MSG_KEY, 64),
            "70:5: " + getCheckMessage(MSG_KEY, 68),
            "71:5: " + getCheckMessage(MSG_KEY, 69),
            "72:5: " + getCheckMessage(MSG_KEY, 70),
            "81:5: " + getCheckMessage(MSG_KEY, 77),
            "83:5: " + getCheckMessage(MSG_KEY, 81),
            "84:5: " + getCheckMessage(MSG_KEY, 82),
            "85:5: " + getCheckMessage(MSG_KEY, 83),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingComplexRegex() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        //^public .*, (protected|package), private, ^static$
        checkConfig.addAttribute("modifierGroups",
            "^public .*, (protected|package), private, ^static$");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "31:5: " + getCheckMessage(MSG_KEY, 27),
            "49:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "70:5: " + getCheckMessage(MSG_KEY, 64),
            "72:5: " + getCheckMessage(MSG_KEY, 68),
            "83:5: " + getCheckMessage(MSG_KEY, 77),
            "85:5: " + getCheckMessage(MSG_KEY, 81),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void groupOverloadsByModifierTestGroupingComplexRegexAsMultipleArgs()
            throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(OverloadMethodsDeclarationOrderCheck.class);
        //^public .*, (protected|package), private, ^static$
        checkConfig.addAttribute("modifierGroups", "^public .*");
        checkConfig.addAttribute("modifierGroups", "(protected|package)");
        checkConfig.addAttribute("modifierGroups", "private");
        checkConfig.addAttribute("modifierGroups", "^static$");
        final String[] expected = {
            "13:5: " + getCheckMessage(MSG_KEY, 11),
            "22:5: " + getCheckMessage(MSG_KEY, 20),
            "31:5: " + getCheckMessage(MSG_KEY, 27),
            "49:5: " + getCheckMessage(MSG_KEY, 45),
            "57:5: " + getCheckMessage(MSG_KEY, 55),
            "70:5: " + getCheckMessage(MSG_KEY, 64),
            "72:5: " + getCheckMessage(MSG_KEY, 68),
            "83:5: " + getCheckMessage(MSG_KEY, 77),
            "85:5: " + getCheckMessage(MSG_KEY, 81),
        };
        verify(checkConfig, getPath("InputOverloadMethodsDeclarationOrder2.java"), expected);
    }

    @Test
    public void testTokensNotNull() {
        final OverloadMethodsDeclarationOrderCheck check =
            new OverloadMethodsDeclarationOrderCheck();
        assertWithMessage("Acceptable tokens should not be null")
            .that(check.getAcceptableTokens())
            .isNotNull();
        assertWithMessage("Default tokens should not be null")
            .that(check.getDefaultTokens())
            .isNotNull();
        assertWithMessage("Required tokens should not be null")
            .that(check.getRequiredTokens())
            .isNotNull();
    }

    @Test
    public void testGetModifierGroupsEmpty() {
        final OverloadMethodsDeclarationOrderCheck check =
            new OverloadMethodsDeclarationOrderCheck();
        Assertions.assertNotNull(check.getModifierGroups());
    }

    @Test
    public void testGetModifierGroupsDefault() {
        final OverloadMethodsDeclarationOrderCheck check =
            new OverloadMethodsDeclarationOrderCheck();
        check.setModifierGroups(".*");
        Assertions.assertNotNull(check.getModifierGroups());
    }

    @Test
    public void testGetModifierGroupsDefaultsCatchAllAndEmptyString() {
        final OverloadMethodsDeclarationOrderCheck check =
            new OverloadMethodsDeclarationOrderCheck();
        check.setModifierGroups(".*");
        check.setModifierGroups("");
        Assertions.assertNotNull(check.getModifierGroups());
    }

    @Test
    public void testGetModifierGroupsDefaults() {
        final OverloadMethodsDeclarationOrderCheck check =
            new OverloadMethodsDeclarationOrderCheck();
        check.setModifierGroups(".*");
        check.setModifierGroups(".*");
        Assertions.assertNotNull(check.getModifierGroups());
    }

    @Test
    public void testGetModifierGroupsCustom() {
        final OverloadMethodsDeclarationOrderCheck check =
            new OverloadMethodsDeclarationOrderCheck();
        check.setModifierGroups("^public .*");
        check.setModifierGroups("(protected|package)");
        check.setModifierGroups("private");
        check.setModifierGroups("^static$");
        check.setModifierGroups(".*");
        Assertions.assertNotNull(check.getModifierGroups());
    }

    @Test
    public void testInnerClassesRegexOverloadGroupEquality() {
        final RegexOverloadGroup regexGroupPublic = new RegexOverloadGroup("public");

        Assertions.assertEquals(DEFAULT_OVERLOAD_GROUP, DEFAULT_OVERLOAD_GROUP,
                                "expecting to be equal due to same instance comparison");
        Assertions.assertEquals(regexGroupPublic, regexGroupPublic,
                                "expecting to be equal due to same instance comparison");
    }

    @Test
    public void testInnerClassesRegexOverloadGroupInEquality() {
        final RegexOverloadGroup regexGroupPrivate = new RegexOverloadGroup("private");
        final RegexOverloadGroup regexGroupPublic = new RegexOverloadGroup("public");

        Assertions.assertNotEquals(regexGroupPublic, null,
                                   "expecting not to be equal due to comparison to null");
        Assertions.assertNotEquals(regexGroupPrivate, regexGroupPublic,
                                   "expecting not to be equal due to different regex pattern");

        Assertions.assertNotEquals(DEFAULT_OVERLOAD_GROUP, null,
                                   "expecting not to be equal due to comparison to null");
        Assertions.assertNotEquals(DEFAULT_OVERLOAD_GROUP, regexGroupPublic,
                                   "expecting to be equal due to different class comparison DefaultOverloadGroup lead");
        Assertions.assertNotEquals(regexGroupPublic, DEFAULT_OVERLOAD_GROUP,
                                   "expecting not to be equal due to different class comparison RegexOverloadGroup lead");
    }

    @Test
    public void testInnerClassesOverloadDescriptorEquality() {
        final RegexOverloadGroup regexGroupProtected = new RegexOverloadGroup("protected");

        final OverloadDescriptor overloadDescriptorProtectedMethod1 =
            new OverloadDescriptor(regexGroupProtected, "method", 1, 1,3);
        final OverloadDescriptor overloadDescriptorProtectedMethod2 =
            new OverloadDescriptor(regexGroupProtected, "method", 1, 1,3);

        Assertions.assertEquals(overloadDescriptorProtectedMethod1, overloadDescriptorProtectedMethod1,
                                "expecting to be equal due to same instance comparison");
        Assertions.assertEquals(overloadDescriptorProtectedMethod2, overloadDescriptorProtectedMethod1,
                                "expecting to be equal due to logical equivalence");
    }

    @Test
    public void testInnerClassesOverloadDescriptorInequality() {
        final RegexOverloadGroup regexGroupProtected = new RegexOverloadGroup("protected");

        final OverloadDescriptor overloadDescriptorProtectedMethod1 =
            new OverloadDescriptor(regexGroupProtected, "method", 1, 1,3);
        final OverloadDescriptor overloadDescriptorProtectedOtherMethod =
            new OverloadDescriptor(regexGroupProtected, "otherMethod", 1, 1,1);
        final OverloadDescriptor overloadDescriptorDefaultOtherMethod = new OverloadDescriptor(
            DEFAULT_OVERLOAD_GROUP, "otherMethod", 1, 1,1);

        Assertions.assertNotEquals(overloadDescriptorProtectedMethod1, overloadDescriptorProtectedOtherMethod,
                                   "expecting not to be equal due to different method name");
        Assertions.assertNotEquals(overloadDescriptorProtectedOtherMethod, null,
                                   "expecting not to be equal due to comparison to null");
        Assertions.assertNotEquals(overloadDescriptorProtectedMethod1, overloadDescriptorDefaultOtherMethod,
                                   "expecting not to be equal due to different overload group and method name");
        Assertions.assertNotEquals(overloadDescriptorDefaultOtherMethod, overloadDescriptorProtectedOtherMethod,
                                   "expecting not to be equal due to different overload group");
    }

    @Test
    public void testInnerClassesOverloadDescriptorDefaultOverLoadGroup() {
        final RegexOverloadGroup regexGroupStatic = new RegexOverloadGroup("static");

        final OverloadDescriptor overloadDescriptorDefaultOtherMethod = new OverloadDescriptor(
            DEFAULT_OVERLOAD_GROUP, "otherMethod", 1, 1,1);

        Assertions.assertNotEquals(overloadDescriptorDefaultOtherMethod, regexGroupStatic,
            "expecting not to be equal due to different overload group and method name");
    }
}
