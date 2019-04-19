/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve.calls.model

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.psi.ValueArgument

class TypeclassValueArgument(val descriptor: ClassDescriptor) : ResolvedValueArgument {
    override fun getArguments(): List<ValueArgument> = emptyList()
}