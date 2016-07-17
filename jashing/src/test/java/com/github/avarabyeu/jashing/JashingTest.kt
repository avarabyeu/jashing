package com.github.avarabyeu.jashing

import com.github.avarabyeu.jashing.core.Jashing


fun main(args: Array<String>) {
    Jashing.builder().withPort(8282).build().bootstrap()
}

