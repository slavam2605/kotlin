/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.descriptors.impl

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor
import org.jetbrains.kotlin.descriptors.ValueDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.resolve.descriptorUtil.isExtensionProperty
import org.jetbrains.kotlin.resolve.scopes.receivers.ExtensionReceiver

class SyntethicWrapperReceiverParameterDescriptor(val contDecl: DeclarationDescriptor, val descriptor: ValueDescriptor)
    : ReceiverParameterDescriptorImpl(contDecl, ExtensionReceiver(descriptor, descriptor.type, null), Annotations.EMPTY) {

}