package xaldarof.dictionary.english.tools

import xaldarof.dictionary.english.domain.models.WordEntity


suspend fun String.clearTrash(wordEntity: suspend (WordEntity) -> Unit) {
    if (contains("]") || contains("[")) {
        val startIndex = indexOf("[")
        val endIndex = indexOf("]")

        val withoutBreak = replaceRange(
            startIndex,
            endIndex + 1,
            " - "
        ).replace("_", "")

        wordEntity.invoke(WordEntity(
            withoutBreak, false
        ))
    }
}
