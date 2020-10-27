package com.github.kimcore.inko

import kotlin.math.floor

@Suppress("unused", "DuplicatedCode")
class Inko(private var allowDoubleConsonant: Boolean = false) {
    private val eng = "rRseEfaqQtTdwWczxvgASDFGZXCVkoiOjpuPhynbmlYUIHJKLBNM"
    private val kor = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎㅁㄴㅇㄹㅎㅋㅌㅊㅍㅏㅐㅑㅒㅓㅔㅕㅖㅗㅛㅜㅠㅡㅣㅛㅕㅑㅗㅓㅏㅣㅠㅜㅡ"
    private val chosung = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ"
    private val jungsung = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ"
    private val jongsung = "ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ"
    private val firstMoeum = 28
    private val rk = 44032
    private val glg = 55203
    private val r = 12593
    private val l = 12643

    companion object {
        private val inko = Inko()
        val String.asKorean: String
            get() = inko.en2ko(this, false)

        val String.asKoreanWithDoubleConsonant: String
            get() = inko.en2ko(this, true)

        val String.asEnglish: String
            get() = inko.ko2en(this)
    }

    private val engIndex = run {
        val x = mutableMapOf<Char, Int>()
        eng.forEachIndexed { i, c -> x[c] = i }
        x
    }

    private val korIndex = run {
        val x = mutableMapOf<Char, Int>()
        kor.forEachIndexed { i, c -> x[c] = i }
        x
    }

    private val connectableConsonant = mapOf(
        "ㄱㅅ" to "ㄳ",
        "ㄴㅈ" to "ㄵ",
        "ㄴㅎ" to "ㄶ",
        "ㄹㄱ" to "ㄺ",
        "ㄹㅁ" to "ㄻ",
        "ㄹㅂ" to "ㄼ",
        "ㄹㅅ" to "ㄽ",
        "ㄹㅌ" to "ㄾ",
        "ㄹㅍ" to "ㄿ",
        "ㄹㅎ" to "ㅀ",
        "ㅂㅅ" to "ㅄ"
    )

    private val connectableVowel = mapOf(
        "ㅗㅏ" to "ㅘ",
        "ㅗㅐ" to "ㅙ",
        "ㅗㅣ" to "ㅚ",
        "ㅜㅓ" to "ㅝ",
        "ㅜㅔ" to "ㅞ",
        "ㅜㅣ" to "ㅟ",
        "ㅡㅣ" to "ㅢ"
    )

    private fun isVowel(e: Char) = korIndex[e]!! >= firstMoeum

    private fun generate(args: MutableList<Int>) =
        (44032 + args[0] * 588 + args[1] * 28 + args[2] + 1).toChar().toString()

    fun config(allowDoubleConsonant: Boolean) {
        this.allowDoubleConsonant = allowDoubleConsonant
    }

    fun en2ko(input: String, allowDoubleConsonant: Boolean = this.allowDoubleConsonant): String {
        val stateLength = arrayOf(0, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5)
        val transitions = arrayOf(
            arrayOf(1, 1, 2, 2),   // 0, EMPTY
            arrayOf(3, 1, 4, 4),   // 1, 자
            arrayOf(1, 1, 5, 2),   // 2, 모
            arrayOf(3, 1, 4, -1),  // 3, 자자
            arrayOf(6, 1, 7, 2),   // 4, 자모
            arrayOf(1, 1, 2, 2),   // 5, 모모
            arrayOf(9, 1, 4, 4),   // 6, 자모자
            arrayOf(9, 1, 2, 2),   // 7, 자모모
            arrayOf(1, 1, 4, 4),   // 8, 자모자자
            arrayOf(10, 1, 4, 4),  // 9, 자모모자
            arrayOf(1, 1, 4, 4)    // 10, 자모모자자
        )

        fun combine(arr: MutableList<Int>): String {
            val group = mutableListOf<MutableList<Char>>()
            for (i in 0 until arr.size) {
                val h = kor[arr[i]]
                if (i == 0 || isVowel(group.last()[0]) != isVowel(h)) group.add(mutableListOf())
                group.last().add(h)
            }

            fun connect(e: MutableList<Char>): String {
                val w = e.joinToString("")
                return when {
                    connectableConsonant.containsKey(w) -> connectableConsonant.getValue(w)
                    connectableVowel.containsKey(w) -> connectableVowel.getValue(w)
                    else -> w
                }
            }

            val group2 = group.map { connect(it) }
            if (group2.size == 1) return group2[0]
            val charSet = arrayOf(chosung, jungsung, jongsung)
            val code = group2.mapIndexed { i, w -> charSet[i].indexOf(w) }.toMutableList()
            if (code.size < 3) code.add(-1)
            return generate(code)
        }

        var last = -1
        fun getLast(): Int = if (last == -1) kor.length - 1 else last
        fun setLast(_last: Int) {
            last = _last
        }

        val result = mutableListOf<String>()
        var state = 0
        var tmp = mutableListOf<Int>()

        fun flush() {
            if (tmp.size > 0) result.add(combine(tmp))
            tmp.clear()
        }

        for (chr in input) {
            val cur = engIndex[chr]
            if (cur == null) {
                state = 0
                flush()
                result.add(chr.toString())
            } else {
                val transition = fun(): Int {
                    val c = (if (kor.getOrNull(getLast()) != null) kor[getLast()].toString() else "") + kor[cur]
                    val lastIsVowel = isVowel(kor[getLast()])
                    val curIsVowel = isVowel(kor[cur])
                    if (!curIsVowel) {
                        if (lastIsVowel) return if ("ㄸㅃㅉ".indexOf(kor[cur]) == -1) 0 else 1
                        if (state == 1 && !allowDoubleConsonant) return 1
                        return if (connectableConsonant.containsKey(c)) 0 else 1
                    } else if (lastIsVowel) return if (connectableVowel.containsKey(c)) 2 else 3
                    return 2
                }()
                val nextState = transitions[state][transition]
                tmp.add(cur)
                val diff = tmp.size - stateLength[nextState]
                if (diff > 0) {
                    result.add(combine(tmp.subList(0, diff)))
                    tmp = tmp.slice(IntRange(diff, tmp.lastIndex)).toMutableList()
                }
                state = nextState
                setLast(cur)
            }
        }
        flush()
        return result.joinToString("")
    }

    fun ko2en(input: String): String {
        var result = ""
        if (input.isEmpty()) return result
        var detached: Array<Int>
        for (char in input) {
            val code = char.toInt()
            if (code in rk..glg || code in r..l) detached = detach(char)
            else {
                result += char
                detached = arrayOf(-1, -1, -1, -1, -1)
            }
            for (i in detached) if (i != -1) result += eng[i]
        }
        return result
    }

    private fun detach(char: Char): Array<Int> {
        return when (val code = char.toInt()) {
            in rk..glg -> {
                val ch = floor((code - rk) / 588.0).toInt()
                val wnd = floor((code - rk - ch * 588) / 28.0).toInt()
                val whd = code - rk - ch * 588 - wnd * 28 - 1
                var wnd1 = wnd
                var wnd2 = -1
                var whd1 = whd
                var whd2 = -1
                when (wnd) {
                    jungsung.indexOf("ㅘ") -> {
                        wnd1 = kor.indexOf("ㅗ")
                        wnd2 = kor.indexOf("ㅏ")
                    }
                    jungsung.indexOf("ㅙ") -> {
                        wnd1 = kor.indexOf("ㅗ")
                        wnd2 = kor.indexOf("ㅐ")
                    }
                    jungsung.indexOf("ㅚ") -> {
                        wnd1 = kor.indexOf("ㅗ")
                        wnd2 = kor.indexOf("ㅣ")
                    }
                    jungsung.indexOf("ㅝ") -> {
                        wnd1 = kor.indexOf("ㅜ")
                        wnd2 = kor.indexOf("ㅓ")
                    }
                    jungsung.indexOf("ㅞ") -> {
                        wnd1 = kor.indexOf("ㅜ")
                        wnd2 = kor.indexOf("ㅔ")
                    }
                    jungsung.indexOf("ㅟ") -> {
                        wnd1 = kor.indexOf("ㅜ")
                        wnd2 = kor.indexOf("ㅣ")
                    }
                    jungsung.indexOf("ㅢ") -> {
                        wnd1 = kor.indexOf("ㅡ")
                        wnd2 = kor.indexOf("ㅣ")
                    }
                }
                when (whd) {
                    jongsung.indexOf("ㄳ") -> {
                        whd1 = kor.indexOf("ㄱ")
                        whd2 = kor.indexOf("ㅅ")
                    }
                    jongsung.indexOf("ㄵ") -> {
                        whd1 = kor.indexOf("ㄴ")
                        whd2 = kor.indexOf("ㅈ")
                    }
                    jongsung.indexOf("ㄶ") -> {
                        whd1 = kor.indexOf("ㄴ")
                        whd2 = kor.indexOf("ㅎ")
                    }
                    jongsung.indexOf("ㄺ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㄱ")
                    }
                    jongsung.indexOf("ㄻ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㅁ")
                    }
                    jongsung.indexOf("ㄼ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㅂ")
                    }
                    jongsung.indexOf("ㄽ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㅅ")
                    }
                    jongsung.indexOf("ㄾ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㅌ")
                    }
                    jongsung.indexOf("ㄿ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㅍ")
                    }
                    jongsung.indexOf("ㅀ") -> {
                        whd1 = kor.indexOf("ㄹ")
                        whd2 = kor.indexOf("ㅎ")
                    }
                    jongsung.indexOf("ㅄ") -> {
                        whd1 = kor.indexOf("ㅂ")
                        whd2 = kor.indexOf("ㅅ")
                    }
                }
                if (wnd2 == -1 && wnd != -1) wnd1 = kor.indexOf(jungsung[wnd]) // 복모음이 아니라면
                if (whd2 == -1 && whd != -1) whd1 = kor.indexOf(jongsung[whd]) // 복자음이 아니라면
                arrayOf(ch, wnd1, wnd2, whd1, whd2)
            }
            in r..l -> {
                when {
                    chosung.indexOf(char) > -1 -> {
                        val ch = kor.indexOf(char)
                        arrayOf(ch, -1, -1, -1, -1)
                    }
                    jungsung.indexOf(char) > -1 -> {
                        val wnd = jungsung.indexOf(char)
                        var wnd1 = wnd
                        var wnd2 = -1
                        when (wnd) {
                            jungsung.indexOf("ㅘ") -> {
                                wnd1 = kor.indexOf("ㅗ")
                                wnd2 = kor.indexOf("ㅏ")
                            }
                            jungsung.indexOf("ㅙ") -> {
                                wnd1 = kor.indexOf("ㅗ")
                                wnd2 = kor.indexOf("ㅐ")
                            }
                            jungsung.indexOf("ㅚ") -> {
                                wnd1 = kor.indexOf("ㅗ")
                                wnd2 = kor.indexOf("ㅣ")
                            }
                            jungsung.indexOf("ㅝ") -> {
                                wnd1 = kor.indexOf("ㅜ")
                                wnd2 = kor.indexOf("ㅓ")
                            }
                            jungsung.indexOf("ㅞ") -> {
                                wnd1 = kor.indexOf("ㅜ")
                                wnd2 = kor.indexOf("ㅔ")
                            }
                            jungsung.indexOf("ㅟ") -> {
                                wnd1 = kor.indexOf("ㅜ")
                                wnd2 = kor.indexOf("ㅣ")
                            }
                            jungsung.indexOf("ㅢ") -> {
                                wnd1 = kor.indexOf("ㅡ")
                                wnd2 = kor.indexOf("ㅣ")
                            }
                        }
                        if (wnd2 == -1) wnd1 = kor.indexOf(jungsung[wnd]) // 복모음이 아니라면
                        arrayOf(-1, wnd1, wnd2, -1, -1)
                    }
                    jongsung.indexOf(char) > -1 -> {
                        val whd = jongsung.indexOf(char)
                        var whd1 = whd
                        var whd2 = -1
                        when (whd) {
                            jongsung.indexOf("ㄳ") -> {
                                whd1 = kor.indexOf("ㄱ")
                                whd2 = kor.indexOf("ㅅ")
                            }
                            jongsung.indexOf("ㄵ") -> {
                                whd1 = kor.indexOf("ㄴ")
                                whd2 = kor.indexOf("ㅈ")
                            }
                            jongsung.indexOf("ㄶ") -> {
                                whd1 = kor.indexOf("ㄴ")
                                whd2 = kor.indexOf("ㅎ")
                            }
                            jongsung.indexOf("ㄺ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㄱ")
                            }
                            jongsung.indexOf("ㄻ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㅁ")
                            }
                            jongsung.indexOf("ㄼ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㅂ")
                            }
                            jongsung.indexOf("ㄽ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㅅ")
                            }
                            jongsung.indexOf("ㄾ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㅌ")
                            }
                            jongsung.indexOf("ㄿ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㅍ")
                            }
                            jongsung.indexOf("ㅀ") -> {
                                whd1 = kor.indexOf("ㄹ")
                                whd2 = kor.indexOf("ㅎ")
                            }
                            jongsung.indexOf("ㅄ") -> {
                                whd1 = kor.indexOf("ㅂ")
                                whd2 = kor.indexOf("ㅅ")
                            }
                        }
                        arrayOf(whd1, whd2, -1, -1, -1)
                    }
                    else -> arrayOf(-1, -1, -1, -1, -1)
                }
            }
            else -> arrayOf(-1, -1, -1, -1, -1)
        }
    }
}