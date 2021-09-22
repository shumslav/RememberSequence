package com.example.application2.UI.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.forEach
import com.example.application2.R
import com.example.application2.Utlis.images
import com.firestu.biggiwins.Settings.BoardSizeSettings
import com.firestu.biggiwins.Settings.GameSettings


class GameFragment : Fragment() {

    companion object {
        private const val LEVEL = "Level"

        @JvmStatic
        fun newInstance(level: Int) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putInt(LEVEL, level)
                }
            }
    }

    lateinit var boardGame: LinearLayout
    lateinit var play: ImageView
    lateinit var gameSettings: GameSettings

    private var level = 1

    private var allColumns = mutableListOf<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            level = it.getInt(LEVEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_game, container, false)
        val boardSizeSettings = BoardSizeSettings(requireContext())
        gameSettings = GameSettings(requireContext())

        boardGame = view.findViewById(R.id.board_game)
        play = view.findViewById(R.id.play)

        boardGame.forEach { allColumns.add(it as LinearLayout) }

        allColumns[0].doOnPreDraw {
            if (boardSizeSettings.size == 0)
                boardSizeSettings.size = it.height
            val game = Game(level)
            game.startGame()
        }

        return view
    }

    inner class Game(private var gameSize: Int) {
        private val boardSizeSettings = BoardSizeSettings(requireContext())
        private val allCards = mutableListOf<Card>()
        private var needingIndex = 1
        private val sequence = mutableListOf<Card>()
        private val difficult = gameSize + 1
        private val margin = when (10 - gameSize > 0) {
            true -> 10 - gameSize
            else -> 0
        }
        private val cardSize = getSize()


        private fun fillBoard() {
            for (columnIndex in 0 until 3) {
                val column = allColumns[columnIndex]
                for (cardIndex in 0 until gameSize) {
                    val card = when (cardIndex != gameSize - 1) {
                        true -> Card(requireContext(), cardSize, margin)
                        else -> Card(requireContext(), cardSize, margin, true)
                    }
                    card.getImageView().isClickable = false
                    column.addView(card.getImageView())
                    allCards.add(card)
                }
            }
        }

        private fun makeSequence() {
            val copyAllCards = mutableListOf<Card>()
            val copyAllImages = mutableListOf<Int>()
            copyAllCards.addAll(allCards)
            for (index in 1..difficult) {
                if (copyAllImages.isEmpty())
                    copyAllImages.addAll(images)
                val randomImage = copyAllImages.random()
                copyAllImages.remove(randomImage)
                val card = copyAllCards.random()
                copyAllCards.remove(card)
                card.index = index
                card.imageId = randomImage
                card.getImageView().tag = randomImage
                sequence.add(card)
            }
            allCards.forEach { card ->
                card.getImageView().setOnClickListener {
                    Log.i("MyTag", "HERE_LISTENER")
                    animateCardOpen(card)
                }
                card.getImageView().isClickable = false
            }
        }

        fun startGame() {
            val animAlphaOne = AnimationUtils.loadAnimation(context, R.anim.alpha_one)
            animAlphaOne.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    fillBoard()
                }

                override fun onAnimationEnd(p0: Animation?) {
                    play.setOnClickListener {
                        makeSequence()
                        animateSequence()
                        play.isClickable = false
                    }
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
            boardGame.startAnimation(animAlphaOne)
        }

        private fun animateSequence(index: Int = 0) {
            val card = sequence[index]
            card.getImageView().animate().withLayer()
                .rotationY(90F)
                .setDuration(500)
                .withEndAction {
                    run() {
                        if (index != difficult - 1)
                            animateSequence(index = index + 1)
                        card.getImageView().setImageResource(card.imageId!!)
                        card.getImageView().rotationY = -90F
                        card.getImageView().animate().withLayer()
                            .rotationY(0F)
                            .setDuration(500)
                            .withEndAction {
                                if (index == difficult - 1)
                                    animateCardClose()
                            }.start()
                    }
                }.start()
        }

        private fun animateCardClose() {
            for (i in 0 until sequence.size) {
                val card = sequence[i]
                card.getImageView().animate().withLayer()
                    .rotationY(-90F)
                    .setDuration(300)
                    .setStartDelay(600)
                    .withEndAction {
                        run() {
                            card.getImageView().setImageResource(R.drawable.close_item)
                            card.getImageView().rotationY = 90F
                            card.getImageView().animate().withLayer()
                                .rotationY(0F)
                                .setDuration(300)
                                .withEndAction {
                                    if (i == difficult - 1)
                                        allCards.forEach { it.getImageView().isClickable = true }
                                }
                                .start()
                        }
                    }.start()
            }
        }

        private fun nextLevel() {
            if (gameSettings.maxLevel==level)
                gameSettings.maxLevel += 1
            gameOver()
        }

        private fun getSize(): Int {
            return ((boardSizeSettings.size - margin * (gameSize - 1)) / gameSize)
        }

        private fun animateCardOpen(card: Card) {
            val index = needingIndex
            card.getImageView().isClickable = false
            if (card.index != index)
                allCards.forEach { it.getImageView().isClickable = false }
            else {
                if (index != difficult)
                    needingIndex += 1
                else
                    allCards.forEach { it.getImageView().isClickable = false }
            }
            val imageView = card.getImageView()
            imageView.animate().withLayer()
                .rotationY(90F)
                .setDuration(300)
                .withStartAction { Log.i("MyTag", "HERE_METHOD") }
                .withEndAction {
                    run() {
                        if (card.getImageView().tag != null)
                            imageView.setImageResource(card.getImageView().tag as Int)
                        else
                            imageView.setImageResource(images.random())
                        imageView.rotationY = -90F
                        imageView.animate().withLayer()
                            .rotationY(0F)
                            .setDuration(300)
                            .withEndAction {
                                if (card.index != index) {
                                    Log.i("MyTagIndex", index.toString())
                                    gameOver()
                                } else {
                                    if (card.index == difficult)
                                        nextLevel()
                                }
                            }.start()
                    }
                }.start()
        }

        private fun gameOver() {
            requireActivity().supportFragmentManager.popBackStack()
        }

        inner class Card(
            private val context: Context,
            private val size: Int,
            private val margin: Int,
            private val isLast: Boolean = false,
            var imageId: Int? = null,
            var index: Int = -1
        ) {

            private val imageView: ImageView

            init {
                imageView = createImageView()
            }

            private fun createImageView(): ImageView {
                val card = ImageView(context)
                val cardParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, size)
                if (!isLast)
                    cardParams.bottomMargin = margin
                card.layoutParams = cardParams
                card.setImageResource(R.drawable.close_item)

                return card
            }

            fun getImageView(): ImageView {
                return imageView
            }
        }
    }
}