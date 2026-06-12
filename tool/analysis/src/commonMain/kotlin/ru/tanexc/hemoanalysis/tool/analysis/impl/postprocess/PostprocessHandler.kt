package ru.tanexc.hemoanalysis.tool.analysis.impl.postprocess

import ru.tanexc.hemoanalysis.tool.analysis.impl.inference.InferenceOutput
import ru.tanexc.hemoanalysis.tool.analysis.impl.data.LetterboxMeta
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.CellClass
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.Detection
import kotlin.collections.plusAssign
import kotlin.math.abs

internal class PostprocessHandler {
    operator fun invoke(
        inferenceOutput: InferenceOutput,
        letterboxMeta: LetterboxMeta,
        confThreshold: Float,
        iouThreshold: Float
    ): PostprocessOutput {
        val detections = process(
            inferenceOutput = inferenceOutput,
            letterboxMeta = letterboxMeta,
            confThreshold = confThreshold,
            iouThreshold = iouThreshold
        )

        return PostprocessOutput(
            detections = detections
        )
    }

    private fun process(
        inferenceOutput: InferenceOutput,
        letterboxMeta: LetterboxMeta,
        confThreshold: Float,
        iouThreshold: Float
    ): List<Detection> {
        val channelCount = inferenceOutput.channelCount
        val candidateCount = inferenceOutput.candidateCount

        val rawPredictions = inferenceOutput.outputData[0]

        val classScoresStartIndex = 4
        val classCount = channelCount - classScoresStartIndex

        val candidates = mutableListOf<Detection>()

        for (candidateIdx in 0 until candidateCount) {
            val x = rawPredictions[0][candidateIdx]
            val y = rawPredictions[1][candidateIdx]
            val width = rawPredictions[2][candidateIdx]
            val height = rawPredictions[3][candidateIdx]

            var bestClassId = -1
            var bestScore = Float.NEGATIVE_INFINITY

            for (classIdx in 0 until classCount) {
                val score = rawPredictions[classScoresStartIndex + classIdx][candidateIdx]

                if (score > bestScore) {
                    bestScore = score
                    bestClassId = classIdx
                }
            }

            if (bestScore < confThreshold) continue
            if (bestClassId !in CellClass.entries.indices) continue

            candidates += Detection(
                cellClass = CellClass.entries[bestClassId],
                confidence = bestScore,
                centerX = x,
                centerY = y,
                width = width,
                height = height
            )
        }

        val originalSpaceDetections = candidates.map { detection ->
            detection.toOriginalSpace(meta = letterboxMeta)
        }

        return applyNms(
            detections = originalSpaceDetections,
            iouThreshold = iouThreshold
        )
    }

    private fun applyNms(
        detections: List<Detection>,
        iouThreshold: Float
    ): List<Detection> {
        return detections
            .groupBy { it.cellClass }
            .flatMap { (_, value) ->
                val remainingItems = value
                    .sortedBy { it.confidence }
                    .toMutableList()

                var index1 = 0

                while (index1 < remainingItems.size) {
                    val currentItem = remainingItems[index1]

                    var index2 = index1 + 1
                    var isDeleted = false

                    while (index2 < remainingItems.size) {
                        val nextItem = remainingItems[index2]

                        if (iou(nextItem, currentItem) > iouThreshold) {
                            remainingItems.removeAt(index1)
                            isDeleted = true
                            break
                        } else {
                            index2++
                        }
                    }

                    if (!isDeleted) {
                        index1++
                    }
                }

                remainingItems.toList()
            }
    }

    private fun Detection.toOriginalSpace(meta: LetterboxMeta): Detection {
        return Detection(
            cellClass = cellClass,
            confidence = confidence,
            centerX = (centerX - meta.padX) / meta.gain,
            centerY = (centerY - meta.padY) / meta.gain,
            width = width / meta.gain,
            height = height / meta.gain
        )
    }

    private fun iou(
        a: Detection,
        b: Detection
    ): Float {
        val interH = ((a.height + b.height) / 2f - abs(a.centerY - b.centerY))
            .coerceAtLeast(0f)

        val interW = ((a.width + b.width) / 2f - abs(a.centerX - b.centerX))
            .coerceAtLeast(0f)

        val interArea = interW * interH

        val areaA = a.width * a.height
        val areaB = b.width * b.height

        val union = (areaA + areaB - interArea)
            .coerceAtLeast(0.0001f)

        return interArea / union
    }
}