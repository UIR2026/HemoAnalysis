package ru.tanexc.hemoanalysis.util


import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import hemoanalysis.shared.generated.resources.Res
import hemoanalysis.shared.generated.resources.allDrawableResources
import hemoanalysis.shared.generated.resources.allStringResources
import hemoanalysis.shared.generated.resources.compose_multiplatform
import hemoanalysis.shared.generated.resources.unknown_resource

fun getDrawableRes(key: String): DrawableResource = Res.allDrawableResources[key]?: Res.drawable.compose_multiplatform

fun getStringRes(key: String): StringResource = Res.allStringResources[key]?: Res.string.unknown_resource