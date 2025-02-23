// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.plugins.gitlab.mergerequest.ui.timeline

import com.intellij.collaboration.ui.SimpleHtmlPane
import com.intellij.collaboration.ui.codereview.CodeReviewTitleUIUtil
import com.intellij.collaboration.ui.util.bindTextHtmlIn
import com.intellij.util.ui.JBFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import org.jetbrains.plugins.gitlab.mergerequest.data.GitLabMergeRequest
import org.jetbrains.plugins.gitlab.util.GitLabBundle
import javax.swing.JComponent

internal object GitLabMergeRequestTimelineTitleComponent {
  fun create(scope: CoroutineScope, mr: GitLabMergeRequest): JComponent {
    return SimpleHtmlPane().apply {
      name = "Review timeline title panel"
      font = JBFont.h2().asBold()
      bindTextHtmlIn(scope, mr.title.map { title ->
        CodeReviewTitleUIUtil.createTitleText(
          title = title,
          reviewNumber = "!${mr.number}",
          url = mr.url,
          tooltip = GitLabBundle.message("open.on.gitlab.tooltip")
        )
      })
    }
  }
}