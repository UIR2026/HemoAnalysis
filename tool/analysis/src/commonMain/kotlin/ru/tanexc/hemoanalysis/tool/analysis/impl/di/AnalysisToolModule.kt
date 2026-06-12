package ru.tanexc.hemoanalysis.tool.analysis.impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.tanexc.hemoanalysis.tool.analysis.impl.AnalysisToolImpl
import ru.tanexc.hemoanalysis.tool.analysis.impl.inference.InferenceHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.postprocess.PostprocessHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.preprocess.PreprocessHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.roi.RoiHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.tensor.TensorHandler
import ru.tanexc.hemoanalysis.tool.analysis.api.AnalysisTool

val analysisToolModule = module {
    singleOf(::PreprocessHandler)
    singleOf(::RoiHandler)
    singleOf(::TensorHandler)
    singleOf(::InferenceHandler)
    singleOf(::PostprocessHandler)
    singleOf(::AnalysisToolImpl) bind AnalysisTool::class
}
