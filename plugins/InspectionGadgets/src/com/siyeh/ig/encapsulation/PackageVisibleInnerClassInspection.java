/*
 * Copyright 2003-2012 Dave Griffith, Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siyeh.ig.encapsulation;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.options.OptPane;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifier;
import com.siyeh.InspectionGadgetsBundle;
import com.siyeh.ig.BaseInspection;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.fixes.MoveClassFix;
import com.siyeh.ig.psiutils.ClassUtils;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInspection.options.OptPane.checkbox;
import static com.intellij.codeInspection.options.OptPane.pane;

public class PackageVisibleInnerClassInspection extends BaseInspection {
  @SuppressWarnings({"PublicField"})
  public boolean ignoreEnums = false;
  @SuppressWarnings("PublicField")
  public boolean ignoreInterfaces = false;

  @Override
  protected LocalQuickFix buildFix(Object... infos) {
    return new MoveClassFix();
  }

  @Override
  @NotNull
  public String buildErrorString(Object... infos) {
    return InspectionGadgetsBundle.message(
      "package.visible.inner.class.problem.descriptor");
  }

  @Override
  public @NotNull OptPane getOptionsPane() {
    return pane(
      checkbox("ignoreEnums", InspectionGadgetsBundle.message("package.visible.inner.class.ignore.enum.option")),
      checkbox("ignoreInterfaces", InspectionGadgetsBundle.message("package.visible.inner.class.ignore.interface.option")));
  }

  @Override
  protected boolean buildQuickFixesOnlyForOnTheFlyErrors() {
    return true;
  }

  @Override
  public BaseInspectionVisitor buildVisitor() {
    return new PackageVisibleInnerClassVisitor();
  }

  private class PackageVisibleInnerClassVisitor extends BaseInspectionVisitor {

    @Override
    public void visitClass(@NotNull PsiClass aClass) {
      if (aClass.hasModifierProperty(PsiModifier.PUBLIC) ||
          aClass.hasModifierProperty(PsiModifier.PROTECTED) ||
          aClass.hasModifierProperty(PsiModifier.PRIVATE)) {
        return;
      }
      if (!ClassUtils.isInnerClass(aClass)) {
        return;
      }
      if (ignoreEnums && aClass.isEnum()) {
        return;
      }
      if (ignoreInterfaces && aClass.isInterface()) {
        return;
      }
      final PsiElement parent = aClass.getParent();
      // parent must be class to not warn on
      // the type parameters of classes, anonymous classes and
      // enum constants for example.
      if (!(parent instanceof PsiClass)) {
        return;
      }
      registerClassError(aClass);
    }
  }
}