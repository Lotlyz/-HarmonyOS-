package com.example.lotday2;

import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;

/**
 * ShadowDrawable
 *
 * @since 2021-04-07
 */
public class ShadowDrawable extends ShapeElement {
    /**
     * 定义一个内部形状圆的变量
     **/
    public static final int SHAPE_ROUND = 1;
    /**
     * 定义一个外部形状圆的变量
     **/
    public static final int SHAPE_CIRCLE = 2;
    private static final int PROPO_RTION = 2;

    // 定义默认值
    private static final int BUILDER_SHADOW_RADIUS = 18;
    private static final int BUILDER_SHAPE_RADIUS = 12;

    private final Paint mShadowPaint;
    private final Paint mBgPaint;
    private final int mShadowRadius;
    private final int mShape;
    private final int mShapeRadius;
    private final int mOffsetX;
    private final int mOffsetY;
    private final int mBgColor[];
    private RectFloat mRect;

    private ShadowDrawable(int shape, int[] bgColor, int shapeRadius,
                           int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        this.mShape = shape;
        this.mBgColor = bgColor;
        this.mShapeRadius = shapeRadius;
        this.mShadowRadius = shadowRadius;
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;

        mShadowPaint = new Paint();
        mShadowPaint.setColor(new Color(shadowColor));
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setMaskFilter(new MaskFilter(shadowRadius, MaskFilter.Blur.NORMAL));
        mShadowPaint.setBlurDrawLooper(new BlurDrawLooper(shadowRadius, mOffsetX, mOffsetY, new Color(shadowColor)));
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRect = new RectFloat(left + mShadowRadius - mOffsetX,
                top + mShadowRadius - mOffsetY, right - mShadowRadius - mOffsetX,
                bottom - mShadowRadius - mOffsetY);
    }

    private void onDraw(Canvas canvas) {
        if (mBgColor != null) {
            if (mBgColor.length == 1) {
                Color color = new Color(mBgColor[0]);
                mBgPaint.setColor(color);
            } else {
                Color[] bgColors = new Color[mBgColor.length];
                for (int i1 = 0; i1 < mBgColor.length; i1++) {
                    Color color1 = new Color(mBgColor[i1]);
                    bgColors[i1] = color1;
                }

                Point[] pointArrays = new Point[]{new Point(mRect.left, mRect.getHeight() / PROPO_RTION),
                        new Point(mRect.right, mRect.getHeight() / PROPO_RTION)};
                LinearShader shader = new LinearShader(pointArrays,null,
                        bgColors, Shader.TileMode.CLAMP_TILEMODE);
                mBgPaint.setShader(shader, Paint.ShaderType.LINEAR_SHADER);
            }
        }

        if (mShape == SHAPE_ROUND) {
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mShadowPaint);
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBgPaint);
        } else {
            canvas.drawCircle(mRect.getHorizontalCenter(), mRect.getVerticalCenter(),
                    Math.min(mRect.getWidth(), mRect.getHeight()) / PROPO_RTION, mShadowPaint);
            canvas.drawCircle(mRect.getHorizontalCenter(), mRect.getVerticalCenter(),
                    Math.min(mRect.getWidth(), mRect.getHeight()) / PROPO_RTION, mBgPaint);
        }
    }

    @Override
    public void drawToCanvas(Canvas canvas) {
        super.drawToCanvas(canvas);
        onDraw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    /**
     * 设置色彩滤镜
     *
     * @param colorFilter 给阴影设置色彩滤镜
     */
    public void setColorFilter(ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    public int getOpacity() {
        return 0;
    }

    /**
     * 设置阴影可绘制
     *
     * @param view     目标View
     * @param drawable 绘制
     */
    public static void setShadowDrawable(Component view, Element drawable) {
        view.setBackground(drawable);
    }

    /**
     * 为指定View添加阴影
     * *
     *
     * @param view         目标View
     * @param shapeRadius  View的圆角
     * @param shadowColor  阴影的颜色
     * @param shadowRadius 阴影的宽度
     * @param offsetX      阴影水平方向的偏移量
     * @param offsetY      阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(Component view, int shapeRadius,
                                         int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawable.setBounds(0, 0, component.getWidth(), component.getHeight());
                drawable.drawToCanvas(canvas);
            }
        }, Component.DrawTask.BETWEEN_BACKGROUND_AND_CONTENT);
        view.setBackground(drawable);
    }

    /**
     * 为指定View设置带阴影的背景
     *
     * @param view         目标View
     * @param bgColor      View背景色
     * @param shapeRadius  View的圆角
     * @param shadowColor  阴影的颜色
     * @param shadowRadius 阴影的宽度
     * @param offsetX      阴影水平方向的偏移量
     * @param offsetY      阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(Component view, int bgColor, int shapeRadius,
                                         int shadowColor, int shadowRadius,
                                         int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawable.setBounds(0, 0, component.getWidth(), component.getHeight());
                drawable.drawToCanvas(canvas);
            }
        }, Component.DrawTask.BETWEEN_BACKGROUND_AND_CONTENT);
        view.setBackground(drawable);
    }

    /**
     * 为指定View设置指定形状并带阴影的背景
     *
     * @param view         目标View
     * @param shape        View的形状 取值可为：GradientDrawable.RECTANGLE， GradientDrawable.OVAL， GradientDrawable.RING
     * @param bgColor      View背景色
     * @param shapeRadius  View的圆角
     * @param shadowColor  阴影的颜色
     * @param shadowRadius 阴影的宽度
     * @param offsetX      阴影水平方向的偏移量
     * @param offsetY      阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(Component view, int shape, int bgColor,
                                         int shapeRadius, int shadowColor,
                                         int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setShape(shape)
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawable.setBounds(0, 0, component.getWidth(), component.getHeight());
                drawable.drawToCanvas(canvas);
            }
        }, Component.DrawTask.BETWEEN_BACKGROUND_AND_CONTENT);
        view.setBackground(drawable);
    }

    /**
     * 为指定View设置带阴影的渐变背景
     *
     * @param view         目标View
     * @param bgColor      View背景色
     * @param shapeRadius  View的圆角
     * @param shadowColor  阴影颜色
     * @param shadowRadius 阴影半径
     * @param offsetX      阴影水平方向的偏移量
     * @param offsetY      阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(Component view, int[] bgColor, int shapeRadius,
                                         int shadowColor, int shadowRadius,
                                         int offsetX, int offsetY) {
        ShadowDrawable drawable = new Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawable.setBounds(0, 0, component.getWidth(), component.getHeight());
                drawable.drawToCanvas(canvas);
            }
        }, Component.DrawTask.BETWEEN_BACKGROUND_AND_CONTENT);
        view.setBackground(drawable);
    }

    /**
     * Builder类
     *
     * @since 2021-04-07
     */
    public static class Builder {
        private int mShape;
        private int mShapeRadius;
        private int mShadowColor;
        private int mShadowRadius;
        private int mOffsetX;
        private int mOffsetY;
        private int[] mBgColor;

        /**
         * 初始化一些形状,形状半径,阴影颜色.阴影半径,水平偏移量,垂直偏移量,背景颜色
         */
        public Builder() {
            mShape = ShadowDrawable.SHAPE_ROUND;
            mShapeRadius = BUILDER_SHAPE_RADIUS;
            mShadowColor = Color.getIntColor("#4d000000");
            mShadowRadius = BUILDER_SHADOW_RADIUS;
            mOffsetX = 0;
            mOffsetY = 0;
            mBgColor = new int[1];
            mBgColor[0] = Color.TRANSPARENT.getValue();
        }

        /**
         * 形状
         *
         * @param mshape   形状大小
         * @return Builder 设置形状
         */
        public Builder setShape(int mshape) {
            this.mShape = mshape;
            return this;
        }

        /**
         * 形状半径
         *
         * @param shapeRadius 圆角大小
         * @return Builder (设置形状半径)
         */
        public Builder setShapeRadius(int shapeRadius) {
            this.mShapeRadius = shapeRadius;
            return this;
        }

        /**
         * 阴影颜色
         *
         * @param shadowColor 阴影颜色
         * @return Builder (设置阴影颜色)
         */
        public Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        /**
         * 阴影半径
         *
         * @param shadowRadius 阴影圆角大小
         * @return Builder (设置阴影半径)
         */
        public Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        /**
         * X轴
         *
         * @param offsetX   X方向偏移量
         * @return Builder (设置偏移量X轴)
         */
        public Builder setOffsetX(int offsetX) {
            this.mOffsetX = offsetX;
            return this;
        }

        /**
         * Y轴
         *
         * @param offsetY  Y方向偏移量
         * @return Builder (设置偏移量Y轴)
         */
        public Builder setOffsetY(int offsetY) {
            this.mOffsetY = offsetY;
            return this;
        }

        /**
         * 背景颜色
         *
         * @param bgColor 背景颜色int类型
         * @return Builder (设置背景颜色)
         */
        public Builder setBgColor(int bgColor) {
            this.mBgColor[0] = bgColor;
            return this;
        }

        /**
         * 背景颜色
         *
         * @param bgColor 背景颜色为int数组
         * @return Builder (设置背景颜色)
         */
        public Builder setBgColor(int[] bgColor) {
            this.mBgColor = bgColor.clone();
            return this;
        }

        /**
         * 构造方法(阴影可绘制)
         *
         * @return ShadowDrawable(绘制)
         */
        public ShadowDrawable builder() {
            return new ShadowDrawable(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mOffsetX, mOffsetY);
        }
    }
}