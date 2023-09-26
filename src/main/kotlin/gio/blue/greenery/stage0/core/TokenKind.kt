package gio.blue.greenery.stage0.core

enum class TokenKind {
    None,

    Annotation,
    Identifier,
    Literal,
    Comment,

    // Comparison
    LesserThan,
    LesserThanOrEqualTo,
    GreaterThan,
    GreaterThanOrEqualTo,
    EqualTo,
    NotEqualTo,

    // Logical (all symbol based)
    And, // &&
    Or, // ||
    Not, // !

    // Bitwise (all symbol based)
    BitwiseAnd, // &
    BitwiseOr, // |
    BitwiseNot, // ~
    BitwiseXor, // ^
    BitwiseLeftShift, // <<
    BitwiseRightShift, // >>

    // Math
    Plus, // +
    Minus, // -
    Multiply, // *
    Power, // **
    Divide, // /
    Modulo, // %

    // Assignment
    Assign, // =
    TargetedPlus, // +=
    TargetedMinus, // -=
    TargetedMultiply, // *=
    TargetedPower, // **=
    TargetedDivide, // /=
    TargetedModulo, // %=
    TargetedBitwiseLeftShift, // <<=
    TargetedBitwiseRightShift, // >>=
    TargetedBitwiseAnd, // &=
    TargetedBitwiseOr, // |=
    TargetedBitwiseXor, // ^=

    // Control flow
    If,
    ElseIf,
    Else,
    For,
    While,
    Break,
    Continue,
    Pass,
    Return,
    Match,

    // Keywords
    As,
    Assert,
    Await,
    Breakpoint,
    Class,
    ClassName,
    Const,
    Enum,
    Extends,
    Func,
    In,
    Is,
    Namespace,
    Preload,
    Self,
    Signal,
    Static,
    Super,
    Trait,
    Var,
    Void,
    Yield,

    // Punctuation
    BracketRoundOpen, // (
    BracketRoundClosed, // )
    BracketSquareOpen, // [
    BracketSquareClosed, // ]
    BracketCurlyOpen, // {
    BracketCurlyClosed, // }
    Comma, // ,
    Semicolon, // ;
    Period, // .
    DoublePeriod, // ..
    Colon, // :
    DollarSign, // $
    ForwardArrow, // ->
    Underscore, // _

    // Whitespace
    LineBreak,
    Indent,
    Dedent,

    // Constants
    ConstPi,
    ConstTau,
    ConstInf,
    ConstNan,

    // Error message improvement (?)
    VcsConflictMarker,
    Backtick,
    QuestionMark
}

