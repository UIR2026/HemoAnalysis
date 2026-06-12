package ru.tanexc.hemoanalysis.tool.analysis.impl

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.AnalysisParameters
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.AnalysisException
import ru.tanexc.hemoanalysis.tool.analysis.impl.inference.InferenceHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.postprocess.PostprocessHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.preprocess.PreprocessHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.roi.RoiHandler
import ru.tanexc.hemoanalysis.tool.analysis.impl.tensor.TensorHandler
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.AnalysisDuration
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.AnalysisResult
import ru.tanexc.hemoanalysis.tool.analysis.api.AnalysisTool
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.measuresDuration

internal class AnalysisToolImpl(
    private val preprocessHandler: PreprocessHandler,
    private val roiHandler: RoiHandler,
    private val tensorHandler: TensorHandler,
    private val inferenceHandler: InferenceHandler,
    private val postprocessHandler: PostprocessHandler,
): AnalysisTool {
    override fun performAnalysis(parameters: AnalysisParameters): AnalysisResult = try {
        val sourceImageBuffer = parameters.imageBuffer

        val (preprocessOutput, preprocessDuration) = measuresDuration {
            preprocessHandler(
                imageBuffer = sourceImageBuffer,
                preprocessVariant = parameters.preprocessVariant
            )
        }

        val (roiOutput, roiDuration) = measuresDuration {
            roiHandler(
                imageBuffer = preprocessOutput.imageBuffer,
                roiVariant =parameters.roiVariant
            )
        }

        val (tensorOutput, tensorDuration) = measuresDuration {
            tensorHandler(imageBuffer = roiOutput.imageBuffer)
        }

        val (inferenceOutput, inferenceDuration) = measuresDuration {
            inferenceHandler(tensorImage = tensorOutput)
        }

        val (postprocessOutput, postprocessDuration) = measuresDuration {
            postprocessHandler(
                inferenceOutput = inferenceOutput,
                letterboxMeta = preprocessOutput.meta,
                confThreshold = parameters.confThreshold,
                iouThreshold = parameters.iouThreshold
            )
        }

        val duration = AnalysisDuration(
            preprocessing = preprocessDuration + roiDuration + tensorDuration,
            inference = inferenceDuration,
            postprocessing = postprocessDuration
        )

        return AnalysisResult.Success(
            detection = postprocessOutput.detections,
            duration = duration
        )
    } catch (e: AnalysisException) {
        return AnalysisResult.Error(e)
    }
}