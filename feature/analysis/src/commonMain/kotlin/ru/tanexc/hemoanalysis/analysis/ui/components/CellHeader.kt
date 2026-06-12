package ru.tanexc.hemoanalysis.analysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.tanexc.hemoanalysis.analysis.ui.strokeColor
import ru.tanexc.hemoanalysis.analysis.ui.title
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.CellClass

@Composable
fun CellHeader(
    cell: CellClass,
    count: Int
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(36.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 16.dp, horizontal = 22.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(cell.strokeColor())
                .align(Alignment.CenterStart)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee()
                .align(Alignment.Center),
            text = cell.title(),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Visible,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            text = count.toString())

    }
}