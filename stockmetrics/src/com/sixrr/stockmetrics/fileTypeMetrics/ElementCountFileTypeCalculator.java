/*
 * Copyright 2005-2016 Sixth and Red River Software, Bas Leijdekkers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sixrr.stockmetrics.fileTypeMetrics;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntProcedure;
import org.jetbrains.annotations.NotNull;

public abstract class ElementCountFileTypeCalculator extends FileTypeCalculator {

    private final TObjectIntHashMap<FileType> elementCountsPerFileType = new TObjectIntHashMap<FileType>();

    @Override
    public void endMetricsRun() {
        elementCountsPerFileType.forEachEntry(new TObjectIntProcedure<FileType>() {
            @Override
            public boolean execute(FileType fileType, int count) {
                postMetric(fileType, count);
                return true;
            }
        });
    }

    public void createElementCount(@NotNull PsiElement element) {
        final PsiFile file = PsiTreeUtil.getParentOfType(element, PsiFile.class, false);
        if (file == null) {
            return;
        }
        final FileType fileType = file.getFileType();
        if (!elementCountsPerFileType.containsKey(fileType)) {
            elementCountsPerFileType.put(fileType, 0);
        }
    }

    protected void incrementElementCount(PsiElement element, int count) {
        final PsiFile file = PsiTreeUtil.getParentOfType(element, PsiFile.class, false);
        if (file == null) {
            return;
        }
        final FileType fileType = file.getFileType();
        if (elementCountsPerFileType.containsKey(fileType)) {
            elementCountsPerFileType.adjustValue(fileType, count);
        }
        else {
            elementCountsPerFileType.put(fileType, count);
        }
    }
}