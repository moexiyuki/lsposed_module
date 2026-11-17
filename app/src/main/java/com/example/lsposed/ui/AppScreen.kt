package com.example.lsposed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lsposed.App
import com.example.lsposed.BuildConfig
import com.example.lsposed.R
import com.example.lsposed.ui.theme.AppTheme
import io.github.libxposed.service.XposedService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen() {
    val service = App.service

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { FrameworkCard(service) }
            item { ScopeCard(service) }
            item { ModuleCard() }
        }
    }
}

@Composable
private fun FrameworkCard(service: XposedService?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "Framework", style = MaterialTheme.typography.titleMedium)
            if (service == null) {
                Text(
                    text = "LSPosed service unavailable. Enable the module and set its scope, then reopen this app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                InfoRow(label = "Name", value = service.frameworkName)
                InfoRow(
                    label = "Version",
                    value = "${service.frameworkVersion} (${service.frameworkVersionCode})",
                )
                InfoRow(label = "API", value = service.apiVersion.toString())
                InfoRow(
                    label = "Capabilities",
                    value = capabilities(service.frameworkProperties)
                        .joinToString()
                        .ifEmpty { "None" },
                )
            }
        }
    }
}

@Composable
private fun ScopeCard(service: XposedService?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "Scope", style = MaterialTheme.typography.titleMedium)
            val scope = service?.scope
            if (scope.isNullOrEmpty()) {
                Text(
                    text = "No scope yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                ScopeTags(packages = scope)
            }
        }
    }
}

@Composable
private fun ModuleCard() {
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "Module", style = MaterialTheme.typography.titleMedium)
            InfoRow(
                label = "Version",
                value = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            )
            InfoRow(label = "Package", value = context.packageName)
            Text(
                text = stringResource(R.string.app_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.5f),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScopeTags(packages: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        packages.forEach { packageName ->
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = packageName,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

private fun capabilities(properties: Long): List<String> = buildList {
    if (properties and XposedService.PROP_CAP_SYSTEM != 0L) add("system")
    if (properties and XposedService.PROP_CAP_REMOTE != 0L) add("remote storage")
    if (properties and XposedService.PROP_RT_API_PROTECTION != 0L) add("API protection")
}

@Preview(showBackground = true)
@Composable
private fun AppScreenPreview() {
    AppTheme {
        AppScreen()
    }
}
