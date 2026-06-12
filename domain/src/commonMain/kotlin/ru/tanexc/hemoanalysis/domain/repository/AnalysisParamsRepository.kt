package ru.tanexc.hemoanalysis.domain.repository

interface AnalysisParamsRepository {
    var confThreshold: Float
    var iouThreshold: Float
    var preprocessVariant: Int
    var roiVariant: Int
}