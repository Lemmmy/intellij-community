// "Remove 'get:' to make the annotation effective. (changes semantics. See: https://youtrack.jetbrains.com/issue/KT-48141)" "true"
// WITH_STDLIB
class Foo {
    private val bar = 0
        <caret>@get /*comment*/   :    Deprecated("", level = DeprecationLevel.ERROR) get
}