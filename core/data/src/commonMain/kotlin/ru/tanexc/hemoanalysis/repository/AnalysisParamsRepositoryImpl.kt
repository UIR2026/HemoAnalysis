package ru.tanexc.hemoanalysis.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.float
import com.russhwolf.settings.int
import ru.tanexc.hemoanalysis.domain.repository.AnalysisParamsRepository

internal class AnalysisParamsRepositoryImpl(
    settings: Settings
): AnalysisParamsRepository {
    override var confThreshold by settings.float("conf_threshold", 0.5f)
    override var iouThreshold by settings.float("iou_threshold", 0.5f)
    override var preprocessVariant by settings.int("preprocess_variant", 0)
    override var roiVariant by settings.int("roi_variant", 0)
}