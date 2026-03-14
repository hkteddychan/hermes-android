package com.hermesandroid.bridge.executor

import android.view.accessibility.AccessibilityNodeInfo
import com.hermesandroid.bridge.model.NodeBounds
import com.hermesandroid.bridge.model.ScreenNode
import com.hermesandroid.bridge.service.BridgeAccessibilityService

object ScreenReader {

    fun readCurrentScreen(includeBounds: Boolean): List<ScreenNode> {
        val service = BridgeAccessibilityService.instance
            ?: return listOf()

        return service.windows
            .flatMap { window -> window.root?.let { listOf(it) } ?: emptyList() }
            .map { root -> buildNode(root, includeBounds) }
    }

    private fun buildNode(info: AccessibilityNodeInfo, includeBounds: Boolean): ScreenNode {
        val rect = if (includeBounds) {
            val r = android.graphics.Rect()
            info.getBoundsInScreen(r)
            NodeBounds(r.left, r.top, r.right, r.bottom)
        } else null

        val nodeId = "${info.packageName ?: "?"}_${info.className ?: "?"}_${info.hashCode()}"

        val children = (0 until info.childCount)
            .mapNotNull { info.getChild(it) }
            .map { buildNode(it, includeBounds) }

        return ScreenNode(
            nodeId = nodeId,
            text = info.text?.toString()?.takeIf { it.isNotBlank() },
            contentDescription = info.contentDescription?.toString()?.takeIf { it.isNotBlank() },
            className = info.className?.toString(),
            packageName = info.packageName?.toString(),
            clickable = info.isClickable,
            focusable = info.isFocusable,
            scrollable = info.isScrollable,
            editable = info.isEditable,
            checked = if (info.isCheckable) info.isChecked else null,
            bounds = rect,
            children = children
        )
    }

    fun findNodeByText(
        text: String,
        exact: Boolean = false
    ): AccessibilityNodeInfo? {
        val service = BridgeAccessibilityService.instance ?: return null
        return service.windows
            .flatMap { it.root?.let { r -> listOf(r) } ?: emptyList() }
            .flatMap { root -> flattenNodes(root) }
            .firstOrNull { node ->
                val nodeText = node.text?.toString() ?: node.contentDescription?.toString() ?: ""
                if (exact) nodeText == text else nodeText.contains(text, ignoreCase = true)
            }
    }

    private fun flattenNodes(root: AccessibilityNodeInfo): List<AccessibilityNodeInfo> {
        val result = mutableListOf<AccessibilityNodeInfo>()
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            result.add(node)
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.add(it) }
            }
        }
        return result
    }
}
