package com.github.kimcore.inko

import com.github.kimcore.inko.Inko.Companion.asEnglish
import com.github.kimcore.inko.Inko.Companion.asKorean
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

object InkoTest {
    @Test
    fun testInko() {
        val inko = Inko()

        // isKorean

        val koreanArray = listOf(
            'ㄱ',
            'ㄴ',
            'ㅇ',
            'ㅎ',
            'ㅍ',
            'ㅋ',
            '기',
            '긹',
            '닙',
            'ㅜ',
            'ㅢ',
            '뷁',
            '챀',
            '팥',
            '가',
            '긯',
        )
        val otherArray = listOf(
            's',
            '1',
            'D',
            '#',
            'R',
            'B',
            'C',
            '9',
            'a',
            ';',
            '=',
            '6',
            '3',
            'P',
        )

        koreanArray.forEach {
            assertEquals(inko.isKorean(it), true)
        }

        otherArray.forEach {
            assertEquals(inko.isKorean(it), false)
        }

        // generate

        assertEquals(inko.generate(0, 0, 0), '각')
        assertEquals(inko.generate(3, 3, 3), '댼')
        assertEquals(inko.generate(1, 10, 10), '꽯')
        assertEquals(inko.generate(4, 6, 8), '뗡')
        assertEquals(inko.generate(14, 15, 13), '췚')

        // detach

        assertEquals(inko.detach('님'), arrayOf(2, 41, -1, 6, -1))
        assertEquals(inko.detach('가'), arrayOf(0, 28, -1, -1, -1))
        assertEquals(inko.detach('뷁'), arrayOf(7, 38, 33, 5, 0))
        assertEquals(inko.detach('없'), arrayOf(11, 32, -1, 7, 9))

        // english > korean (allowDoubleConsonant: false)

        assertEquals(inko.en2ko("dkssud"), "안녕")
        assertEquals(inko.en2ko("dkssudgktpdy"), "안녕하세요")
        assertEquals(inko.en2ko("rkskekfk"), "가나다라")
        assertEquals(inko.en2ko("rldjrsktpdy?"), "기억나세요?")
        assertEquals(inko.en2ko("anjgoqnpfrqnpfr"), "뭐해뷁뷁")
        assertEquals(inko.en2ko("anjgktpdy"), "뭐하세요")
        assertEquals(inko.en2ko("apfhd"), "메롱")
        assertEquals(inko.en2ko("fnffnfkffk"), "룰루랄라")
        assertEquals(
            inko.en2ko("rldjr wjvusdp sjrk todrkrsk"),
            "기억 저편에 너가 생각나"
        )
        assertEquals(inko.en2ko("dmlrlthcla"), "의기소침")
        assertEquals(inko.en2ko("dho wjgksxp rmfjtpdy?"), "왜 저한테 그러세요?")
        assertEquals(inko.en2ko("woalTwlaks woaldjqtek."), "재밌지만 재미없다.")
        assertEquals(
            inko.en2ko("dbfrhrdldltjstodsladms djswpsk skgksxp clswjfgktuTek."),
            "율곡이이선생님은 언제나 나한테 친절하셨다."
        )
        assertEquals(
            inko.en2ko("alclwldksgrhtjdi rmfjf tn djqtdmf rjtdlek."),
            "미치지않고서야 그럴 수 없을 것이다."
        )
        assertEquals(inko.en2ko("difralqtWyfq"), "얅밊쬷")
        assertEquals(inko.en2ko("diffkfldiffkdtud"), "얄라리얄랑셩")
        assertEquals(inko.en2ko("DKSSUD"), "안녕")
        assertEquals(inko.en2ko("dUDn"), "여우")
        assertEquals(inko.en2ko("rrrr"), "ㄱㄱㄱㄱ")
        assertEquals(inko.en2ko("hhhh"), "ㅗㅗㅗㅗ")
        assertEquals(
            inko.en2ko("r s e f a q t d w c z x v g"),
            "ㄱ ㄴ ㄷ ㄹ ㅁ ㅂ ㅅ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ"
        )
        assertEquals(inko.en2ko("hlhl"), "ㅚㅚ")
        assertEquals(inko.en2ko("QlEkrgkrp"), "삐딱하게")
        assertEquals(
            inko.en2ko("ekfkawnl gjs cptqkznldp xkrhvk"),
            "다람쥐 헌 쳇바퀴에 타고파"
        )
        assertEquals(
            inko.en2ko("EKFKAWNL GJS CPTQKZNLDP XKRHVK"),
            "따람쮜 헌 촀빠퀴예 타꼬파"
        )
        assertEquals(inko.en2ko("rtk"), "ㄱ사")

        // different by allowDoubleConsonant option
        assertEquals(inko.en2ko("rtrt"), "ㄱㅅㄱㅅ")
        assertEquals(
            inko.en2ko("rtrt", false),
            "ㄱㅅㄱㅅ"
        )
        assertEquals(
            inko.en2ko("rsefaqtdwczxvg", false),
            "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ"
        )

        // english > korean (allowDoubleConsonant: true)

        assertEquals(inko.en2ko("dkssud", true), "안녕")
        assertEquals(inko.en2ko("dkssudgktpdy", true), "안녕하세요")
        assertEquals(inko.en2ko("rkskekfk", true), "가나다라")
        assertEquals(inko.en2ko("rldjrsktpdy?", true), "기억나세요?")
        assertEquals(inko.en2ko("anjgoqnpfrqnpfr", true), "뭐해뷁뷁")
        assertEquals(inko.en2ko("anjgktpdy", true), "뭐하세요")
        assertEquals(inko.en2ko("apfhd", true), "메롱")
        assertEquals(inko.en2ko("fnffnfkffk", true), "룰루랄라")
        assertEquals(
            inko.en2ko("rldjr wjvusdp sjrk todrkrsk", true),
            "기억 저편에 너가 생각나"
        )
        assertEquals(inko.en2ko("dmlrlthcla", true), "의기소침")
        assertEquals(
            inko.en2ko("dho wjgksxp rmfjtpdy?", true),
            "왜 저한테 그러세요?"
        )
        assertEquals(
            inko.en2ko("woalTwlaks woaldjqtek.", true),
            "재밌지만 재미없다."
        )
        assertEquals(
            inko.en2ko("dbfrhrdldltjstodsladms djswpsk skgksxp clswjfgktuTek.", true),
            "율곡이이선생님은 언제나 나한테 친절하셨다."
        )
        assertEquals(
            inko.en2ko("alclwldksgrhtjdi rmfjf tn djqtdmf rjtdlek.", true),
            "미치지않고서야 그럴 수 없을 것이다."
        )
        assertEquals(inko.en2ko("difralqtWyfq", true), "얅밊쬷")
        assertEquals(inko.en2ko("diffkfldiffkdtud", true), "얄라리얄랑셩")
        assertEquals(inko.en2ko("DKSSUD", true), "안녕")
        assertEquals(inko.en2ko("dUDn", true), "여우")
        assertEquals(inko.en2ko("rrrr", true), "ㄱㄱㄱㄱ")
        assertEquals(inko.en2ko("hhhh", true), "ㅗㅗㅗㅗ")
        assertEquals(
            inko.en2ko("r s e f a q t d w c z x v g", true),
            "ㄱ ㄴ ㄷ ㄹ ㅁ ㅂ ㅅ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ"
        )
        assertEquals(inko.en2ko("hlhl", true), "ㅚㅚ")
        assertEquals(inko.en2ko("QlEkrgkrp", true), "삐딱하게")
        assertEquals(
            inko.en2ko("ekfkawnl gjs cptqkznldp xkrhvk", true),
            "다람쥐 헌 쳇바퀴에 타고파"
        )
        assertEquals(
            inko.en2ko("EKFKAWNL GJS CPTQKZNLDP XKRHVK", true),
            "따람쮜 헌 촀빠퀴예 타꼬파"
        )
        assertEquals(inko.en2ko("rtk", true), "ㄱ사")

        // different by allowDoubleConsonant option
        assertEquals(inko.en2ko("rtrt", true), "ㄳㄳ")
        assertEquals(inko.en2ko("rsefaqtdwczxvg", true), "ㄱㄴㄷㄻㅄㅇㅈㅊㅋㅌㅍㅎ")

        // korean > english
        assertEquals(inko.ko2en("ㅗ디ㅣㅐ"), "hello")
        assertEquals(inko.ko2en("ㅗ디ㅣㅐ 재깅!"), "hello world!")
        assertEquals(
            inko.ko2en("ㅡㅛ ㄹ갸둥 ㅑㄴ ㅗ뭉내ㅡㄷ"),
            "my friend is handsome"
        )
        assertEquals(inko.ko2en("ㄱㄱㄱㄱ"), "rrrr")
        assertEquals(inko.ko2en("안녕하세요"), "dkssudgktpdy")
        assertEquals(inko.ko2en("안녕하십니까"), "dkssudgktlqslRk")
        assertEquals(inko.ko2en("ㄲㄲㄲㄲㄲ"), "RRRRR")
        assertEquals(inko.ko2en("앍얽웺딗"), "dkfrdjfrdnpqtelrt")
        assertEquals(inko.ko2en("ㄺㅄ"), "frqt")
        assertEquals(inko.ko2en("ㄹ"), "f")
        assertEquals(inko.ko2en("ㄻ"), "fa")
        assertEquals(inko.ko2en("ㄻㄱ"), "far")
        assertEquals(inko.ko2en("ㄳ"), "rt")
        assertEquals(inko.ko2en("ㄻㄳㄲ"), "fartR")
        assertEquals(inko.ko2en("ㅁㄴㅇㄻㄴㅇㄹ"), "asdfasdf")
    }
}