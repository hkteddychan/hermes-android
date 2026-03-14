package com.hermesandroid.bridge.model

data class ScreenNode(
    val nodeId: String,
    val text: String?,
    val contentDescription: String?,
    val className: String?,
    val packageName: String?,
    val clickable: Boolean,
    val focusable: Boolean,
    val scrollable: Boolean,
    val editable: Boolean,
    val checked: Boolean?,
    val bounds: NodeBounds?,
    val children: List<ScreenNode> = emptyList()
)

data class NodeBounds(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    val centerX: Int get() = (left + right) / 2
    val centerY: Int get() = (top + bottom) / 2
}

data class ActionResult(
    val success: Boolean,
    val message: String = "",
    val data: Any? = null
)
