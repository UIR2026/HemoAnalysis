package ru.tanexc.hemoanalysis.analysis.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import hemoanalysis.feature.analysis.generated.resources.Res
import hemoanalysis.feature.analysis.generated.resources.cell_class_artifacts
import hemoanalysis.feature.analysis.generated.resources.cell_class_bad_cells
import hemoanalysis.feature.analysis.generated.resources.cell_class_basophil
import hemoanalysis.feature.analysis.generated.resources.cell_class_blast
import hemoanalysis.feature.analysis.generated.resources.cell_class_eosinophil
import hemoanalysis.feature.analysis.generated.resources.cell_class_gumpricht_shadows
import hemoanalysis.feature.analysis.generated.resources.cell_class_lymphocyte
import hemoanalysis.feature.analysis.generated.resources.cell_class_macrophages
import hemoanalysis.feature.analysis.generated.resources.cell_class_magakariocyte
import hemoanalysis.feature.analysis.generated.resources.cell_class_metamyelocyte
import hemoanalysis.feature.analysis.generated.resources.cell_class_methoses
import hemoanalysis.feature.analysis.generated.resources.cell_class_monocyte
import hemoanalysis.feature.analysis.generated.resources.cell_class_myelocyte
import hemoanalysis.feature.analysis.generated.resources.cell_class_normoblasts
import hemoanalysis.feature.analysis.generated.resources.cell_class_normoblasts_basophilic
import hemoanalysis.feature.analysis.generated.resources.cell_class_normoblasts_erythroblasts
import hemoanalysis.feature.analysis.generated.resources.cell_class_normoblasts_oxyfilny
import hemoanalysis.feature.analysis.generated.resources.cell_class_normoblasts_polychromatophilic
import hemoanalysis.feature.analysis.generated.resources.cell_class_plasma_cell
import hemoanalysis.feature.analysis.generated.resources.cell_class_promyelocyte
import hemoanalysis.feature.analysis.generated.resources.cell_class_rod_neutrophil
import hemoanalysis.feature.analysis.generated.resources.cell_class_segmentonuclear_neutrophil
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.CellClass


fun CellClass.strokeColor(): Color = when (this) {
    CellClass.Blast -> Color(0xFFD50000)
    CellClass.BadCells -> Color(0xFFFF6D00)
    CellClass.Promyelocyte -> Color(0xFFC62828)
    CellClass.Myelocyte -> Color(0xFFAD1457)
    CellClass.Metamyelocyte -> Color(0xFF6A1B9A)

    CellClass.RodNeutrophil -> Color(0xFF1565C0)
    CellClass.SegmentonuclearNeutrophil -> Color(0xFF00838F)

    CellClass.Lymphocyte -> Color(0xFF2E7D32)
    CellClass.Monocyte -> Color(0xFF00695C)
    CellClass.Eosinophil -> Color(0xFFF9A825)
    CellClass.Basophil -> Color(0xFF283593)
    CellClass.Macrophages -> Color(0xFF00897B)
    CellClass.PlasmaCell -> Color(0xFF8E24AA)

    CellClass.Magakariocyte -> Color(0xFF5D4037)
    CellClass.Methoses -> Color(0xFF4E342E)

    CellClass.Normoblasts -> Color(0xFF3949AB)
    CellClass.NormoblastsOxyfilny -> Color(0xFFFF8A65)
    CellClass.NormoblastsErythroblasts -> Color(0xFFE53935)
    CellClass.NormoblastsBasophilic -> Color(0xFF1E88E5)
    CellClass.NormoblastsPolychromatophilic -> Color(0xFF8E24AA)

    CellClass.Artifacts -> Color(0xFF757575)
    CellClass.GumprichtShadows -> Color(0xFF9E9E9E)
}

@Composable
fun CellClass.title() = when (this) {
    CellClass.Lymphocyte -> stringResource(Res.string.cell_class_lymphocyte)
    CellClass.Metamyelocyte -> stringResource(Res.string.cell_class_metamyelocyte)
    CellClass.Monocyte -> stringResource(Res.string.cell_class_monocyte)
    CellClass.Myelocyte -> stringResource(Res.string.cell_class_myelocyte)
    CellClass.Normoblasts -> stringResource(Res.string.cell_class_normoblasts)
    CellClass.RodNeutrophil -> stringResource(Res.string.cell_class_rod_neutrophil)
    CellClass.SegmentonuclearNeutrophil -> stringResource(Res.string.cell_class_segmentonuclear_neutrophil)
    CellClass.Blast -> stringResource(Res.string.cell_class_blast)
    CellClass.Promyelocyte -> stringResource(Res.string.cell_class_promyelocyte)
    CellClass.Eosinophil -> stringResource(Res.string.cell_class_eosinophil)
    CellClass.Basophil -> stringResource(Res.string.cell_class_basophil)
    CellClass.Magakariocyte -> stringResource(Res.string.cell_class_magakariocyte)
    CellClass.BadCells -> stringResource(Res.string.cell_class_bad_cells)
    CellClass.PlasmaCell -> stringResource(Res.string.cell_class_plasma_cell)
    CellClass.Artifacts -> stringResource(Res.string.cell_class_artifacts)
    CellClass.GumprichtShadows -> stringResource(Res.string.cell_class_gumpricht_shadows)
    CellClass.Methoses -> stringResource(Res.string.cell_class_methoses)
    CellClass.NormoblastsOxyfilny -> stringResource(Res.string.cell_class_normoblasts_oxyfilny)
    CellClass.Macrophages -> stringResource(Res.string.cell_class_macrophages)
    CellClass.NormoblastsErythroblasts -> stringResource(Res.string.cell_class_normoblasts_erythroblasts)
    CellClass.NormoblastsBasophilic -> stringResource(Res.string.cell_class_normoblasts_basophilic)
    CellClass.NormoblastsPolychromatophilic -> stringResource(Res.string.cell_class_normoblasts_polychromatophilic)
}