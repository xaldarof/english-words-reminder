package xaldarof.dictionary.english.tools

fun String.clearTrash():String {
    return replace("_n.", "").replace(">", ": ")
        .replace("_фр.", "").replace("_а.", "").replace("_a.", "")
        .replaceFirst("_бот.", "")
        .replaceFirst("1.", "")
        .replace("_жд.", "")
}