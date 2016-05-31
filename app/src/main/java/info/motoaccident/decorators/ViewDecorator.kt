package info.motoaccident.decorators

interface ViewDecorator<A> {
    fun start(target: A)
    fun stop()
}