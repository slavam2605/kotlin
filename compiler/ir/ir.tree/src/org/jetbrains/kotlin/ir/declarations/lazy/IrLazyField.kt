/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.declarations.lazy

import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.symbols.IrFieldSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.DeclarationStubGenerator
import org.jetbrains.kotlin.ir.util.TypeTranslator
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.isEffectivelyExternal

class IrLazyField(
    startOffset: Int,
    endOffset: Int,
    origin: IrDeclarationOrigin,
    override val symbol: IrFieldSymbol,
    override val name: Name,
    override val visibility: Visibility,
    override val isFinal: Boolean,
    override val isExternal: Boolean,
    override val isStatic: Boolean,
    stubGenerator: DeclarationStubGenerator,
    typeTranslator: TypeTranslator
) :
    IrLazyDeclarationBase(startOffset, endOffset, origin, stubGenerator, typeTranslator),
    IrField {

    init {
        symbol.bind(this)
    }

    override val descriptor: PropertyDescriptor
        get() = symbol.descriptor

    override val type: IrType by lazyVar {
        descriptor.returnType!!.toIrType()
    }

    override var initializer: IrExpressionBody? = null

    @Suppress("OverridingDeprecatedMember")
    override var correspondingProperty: IrProperty?
        get() = correspondingPropertySymbol?.owner
        set(value) {
            correspondingPropertySymbol = value?.symbol
        }

    override var correspondingPropertySymbol: IrPropertySymbol? = null

    override val overriddenSymbols: MutableList<IrFieldSymbol> by lazy {
        descriptor.overriddenDescriptors.mapTo(arrayListOf()) {
            stubGenerator.generateFieldStub(it.original).symbol
        }
    }

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R =
        visitor.visitField(this, data)

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        initializer?.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        initializer = initializer?.accept(transformer, data) as IrExpressionBody?
    }

    companion object {
        fun fromSymbolDescriptor(
            startOffset: Int,
            endOffset: Int,
            origin: IrDeclarationOrigin,
            symbol: IrFieldSymbol,
            stubGenerator: DeclarationStubGenerator,
            typeTranslator: TypeTranslator
        ): IrLazyField {
            val descriptor = symbol.descriptor
            return IrLazyField(
                startOffset, endOffset, origin,
                symbol,
                descriptor.name,
                descriptor.visibility,
                !descriptor.isVar,
                descriptor.isEffectivelyExternal(),
                descriptor.dispatchReceiverParameter == null,
                stubGenerator, typeTranslator
            )
        }
    }
}