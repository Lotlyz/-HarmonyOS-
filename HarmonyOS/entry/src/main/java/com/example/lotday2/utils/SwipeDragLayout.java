package com.example.lotday2.utils;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DragInfo;
import ohos.agp.components.StackLayout;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Created by ditclear on 16/7/12. 可滑动的layout extends FrameLayout
 */
public class SwipeDragLayout extends StackLayout implements Component.TouchEventListener,
        Component.BindStateChangedListener, Component.DraggedListener {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "SwipeDragLayout");
    private static final String LEFT = "left";
    private static final int ZERO = 0;
    private static final int TIME = 200;
    private SwipeListContainerListener swipeListContainerListener;
    private SwipeListener swipeListener;
    private Component mComponentSlideOver;
    private Component mComponentSlide;
    private AnimatorProperty mSlideAnimator;
    private AnimatorProperty mSlideOverAnimator;
    private Context mContext;
    private float slideX;
    private float slideOver;
    private float moveX;
    private String mLeftRight; // 方向
    private int mBackDistance; // 回弹距离
    private double needOffset;
    private boolean isSpringBack; // 回弹
    private boolean isSliding; // 是否可滑动
    private double mDownX;
    private boolean isOpenCloseLeft;
    private boolean isLtR;
    private boolean isLeft;
    private boolean isRight;
    private double offsetRatio;

    /**
     * 构造
     *
     * @param context 上下文
     */
    public SwipeDragLayout(Context context) {
        super(context);
    }

    /**
     * 构造
     *
     * @param context 上下文
     * @param attrSet 属性集
     */
    public SwipeDragLayout(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context, attrSet);
    }

    private void init(Context context, AttrSet attrSet) {
        mContext = context;
        setTouchEventListener(this);
        setBindStateChangedListener(this);
        isSpringBack = TypedAttrUtils.getBoolean(attrSet, "spring_back", false);
        isSliding = TypedAttrUtils.getBoolean(attrSet, "swipe_enable", false);
        mLeftRight = TypedAttrUtils.getString(attrSet, "swipe_direction", LEFT);
        needOffset = TypedAttrUtils.getInt(attrSet,"need_offset",2);
        setDraggedListener(DRAG_HORIZONTAL, this);
    }

    public void setSwipeListContainerListener(SwipeListContainerListener listContainerListener) {
        this.swipeListContainerListener = listContainerListener;
    }

    public void setSwipeListener(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        component.setEnabled(isSliding);
        MmiPoint pointerPosition = touchEvent.getPointerPosition(ZERO);
        if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
            mDownX = pointerPosition.getX();
        } else if (touchEvent.getAction() == TouchEvent.POINT_MOVE) {
            moveX = pointerPosition.getX();
        } else if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP) {
            if (!(mDownX == moveX) && !(mDownX == pointerPosition.getX())
                    && mComponentSlideOver.getContentPositionX() != 0) {
                mComponentSlideOver.setEnabled(true);
                if (isLeftRight()) {
                    leftRightUp();
                } else {
                    rightLeftUp();
                }
            } else {
                selectedDown();
            }
        }
        if (isSliding) {
            return true;
        } else {
            return false;
        }
    }

    private void selectedDown() {
        int[] mOnScreen = mComponentSlide.getLocationOnScreen();
        if (mComponentSlideOver.getContentPositionX() != 0 && leftRightX(mOnScreen[0])) {
            mComponentSlideOver.setEnabled(false);
            slideDown();
        }
    }

    private boolean leftRightX(int downX) {
        return isLeftRight() ? mDownX < downX : mDownX > mComponentSlide.getEstimatedWidth();
    }

    private void slideDown() {
        if (mComponentSlideOver.getContentPositionX() != 0) {
            if (isLeftRight()) {
                isLeft = false;
                animationToX();
            } else {
                isRight = false;
                animationRightFrom();
            }
        }
    }

    private void animationRightFrom() {
        mSlideAnimator.moveFromX(mComponentSlide.getContentPositionX())
                .moveToX(-mComponentSlide.getEstimatedWidth()).setDuration(TIME).start();
        mSlideOverAnimator.moveFromX(mComponentSlideOver.getContentPositionX()).moveToX(ZERO).setDuration(TIME).start();
    }

    /**
     * 右-->左(right)
     *
     * @param rightX x轴
     */
    private void openRight(double rightX) {
        isOpen();
        contSlide(rightX);
        if (slideOver >= mComponentSlide.getEstimatedWidth()) {
            springBack();
            if (!isSpringBack) {
                mComponentSlide.setContentPositionX(ZERO);
                mComponentSlideOver.setContentPositionX(mComponentSlide.getEstimatedWidth());
            }
        } else {
            openCloseX();
        }
    }

    private void closeLeft(double leftX) {
        contSlide(leftX);
        if (slideOver >= 0) {
            mComponentSlide.setContentPositionX(getScreenWidth());
            mComponentSlideOver.setContentPositionX(0);
        } else {
            closeSlide();
        }
    }

    private void closeSlide() {
        if (isSpringBack && Math.abs(slideOver) > mComponentSlide.getEstimatedWidth()) {
            mComponentSlideOver.setContentPositionX(slideOver);
        } else {
            openCloseX();
        }
    }

    public void setNeedOffset(int offset) {
        this.needOffset = offset;
    }

    private void closeRight(double rightX) {
        contSlide(rightX);
        if (slideOver >= 0) {
            closeSlide();
        }
    }

    private void openCloseX() {
        mComponentSlide.setContentPositionX(slideX);
        mComponentSlideOver.setContentPositionX(slideOver);
    }

    private void openLeft(double leftX) {
        isOpen();
        contSlide(leftX);
        if (Math.abs(slideOver) >= mComponentSlide.getEstimatedWidth()) {
            springBack();
            if (!isSpringBack) {
                mComponentSlide.setContentPositionX(slidingDistance());
                mComponentSlideOver.setContentPositionX(-mComponentSlide.getEstimatedWidth());
            }
        } else {
            openCloseX();
        }
    }

    private void isOpen() {
        if (isOpenCloseLeft) {
            if (swipeListContainerListener != null) {
                isOpenCloseLeft = false;
                swipeListContainerListener.openSwipe(this);
            }
        }
    }

    private void contSlide(double leftX) {
        slideOver = slideOver(leftX);
        slideX = slideValue(leftX);
    }

    private void springBack() {
        if (isSpringBack && Math.abs(slideOver) < mBackDistance) {
            mComponentSlideOver.setContentPositionX(slideOver);
        }
    }

    private void rightLeftUp() {
        if (mDownX - moveX < 0) {
            if (mComponentSlideOver.getContentPositionX() > distance()) {
                springBackRight();
            } else {
                animationRightFrom();
            }
        } else {
            isRight = false;
            if ((mComponentSlide.getEstimatedWidth() - mComponentSlideOver.getContentPositionX()) > distance()) {
                animationRightFrom();
                animationCloseListener();
            } else {
                animationRightTo();
            }
        }
    }

    private void springBackRight() {
        if (!isRight) {
            isRight = true;
            animationOpenListener();
            animationRightTo();
        } else {
            if (isSpringBack) {
                animationRightTo();
            } else {
                isRight = false;
                animationRightFrom();
            }
        }
    }

    private void animationRightTo() {
        mSlideAnimator.moveFromX(mComponentSlide.getContentPositionX()).moveToX(ZERO).setDuration(TIME).start();
        mSlideOverAnimator.moveFromX(mComponentSlideOver.getContentPositionX())
                .moveToX(mComponentSlide.getEstimatedWidth()).setDuration(TIME).start();
    }

    private void leftRightUp() {
        if (mDownX - moveX > 0) {
            if ((getScreenWidth() - mComponentSlide.getContentPositionX()) > distance()) {
                springBackAnimation();
            } else {
                animationToX();
            }
        } else {
            isLeft = false;
            if ((mComponentSlideOver.getContentPositionX() + mComponentSlide.getEstimatedWidth()) > distance()) {
                animationCloseListener();
                animationToX();
            } else {
                animationFromX();
            }
        }
    }

    private double distance() {
        return mComponentSlide.getEstimatedWidth() * (needOffset / 10);
    }

    private void springBackAnimation() {
        if (!isLeft) {
            isLeft = true;
            animationFromX();
            animationOpenListener();
        } else {
            if (isSpringBack) {
                animationFromX();
            } else {
                isLeft = false;
                animationToX();
            }
        }
    }

    private void animationCloseListener() {
        if (swipeListener != null) {
            swipeListener.onClosed(this);
        }
    }

    private void animationOpenListener() {
        if (swipeListener != null) {
            swipeListener.onOpened(this);
        }
    }

    private void animationFromX() {
        if (mComponentSlide.getContentPositionX() < getScreenWidth()) {
            mSlideAnimator.moveFromX(mComponentSlide.getContentPositionX())
                    .moveToX(slidingDistance()).setDuration(TIME).start();
            mSlideOverAnimator.moveFromX(mComponentSlideOver.getContentPositionX())
                    .moveToX(-mComponentSlide.getEstimatedWidth()).setDuration(TIME).start();
        }
    }

    private void animationToX() {
        mSlideAnimator.moveFromX(mComponentSlide.getContentPositionX()).moveToX(getScreenWidth())
                .setCurveType(Animator.CurveType.LINEAR).setDuration(TIME).start();
        mSlideOverAnimator.moveFromX(mComponentSlideOver.getContentPositionX()).moveToX(0)
                .setCurveType(Animator.CurveType.LINEAR).setDuration(TIME).start();
    }

    private boolean isLeftRight() {
        return mLeftRight.equals(LEFT);
    }

    private float slideValue(double rightX) {
        return mComponentSlide.getContentPositionX() + Math.round(rightX);
    }

    private float slideOver(double rightX) {
        return mComponentSlideOver.getContentPositionX() + Math.round(rightX);
    }

    private int getScreenWidth() {
        return DisplayManager.getInstance().getDefaultDisplay(mContext).get().getAttributes().width;
    }

    /**
     * 配合ListContainer使用，使上一个item的菜单关闭
     */
    public void setListSlideDown() {
        slideDown();
    }

    /**
     * 设置左右
     *
     * @param lfRt 左右
     */
    public void setSwipeDirection(String lfRt) {
        this.mLeftRight = lfRt;
    }

    /**
     * 回弹效果
     *
     * @param isBack 是否开启
     */
    public void setOhos(boolean isBack) {
        isSpringBack = isBack;
    }

    /**
     * 是否可滑动
     *
     * @param isSlide 是否滑动
     */
    public void setSwipeEnable(boolean isSlide) {
        this.isSliding = isSlide;
    }

    /**
     * 是否开启水平布局左右调换
     *
     * @param isLr 默认false开启
     */
    public void setLtR(boolean isLr) {
        this.isLtR = isLr;
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        mComponentSlide = getComponentAt(1);
        mComponentSlideOver = getComponentAt(ZERO);
        mSlideAnimator = mComponentSlide.createAnimatorProperty();
        mSlideOverAnimator = mComponentSlideOver.createAnimatorProperty();
        position();
    }

    private int slidingDistance() {
        return getScreenWidth() - mComponentSlide.getEstimatedWidth();
    }

    private void position() {
        if (isLeftRight()) {
            mComponentSlide.setMarginLeft(getScreenWidth());
        } else {
            mComponentSlide.setMarginLeft(-mComponentSlide.getEstimatedWidth());
        }
        mBackDistance = mComponentSlide.getEstimatedWidth() * 4 / 3;
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
    }

    @Override
    public void onDragDown(Component component, DragInfo dragInfo) {
        position();
        if (!mLeftRight.equals(LEFT) && !mLeftRight.equals("right")) {
            component.setEnabled(false);
        }
        if (!isLtR) {
            mComponentSlide.setLayoutDirection(isLeftRight() ? LayoutDirection.LTR : LayoutDirection.RTL);
        } else {
            mComponentSlide.setLayoutDirection(isLeftRight() ? LayoutDirection.RTL : LayoutDirection.LTR);
        }
        if (swipeListContainerListener != null) {
            swipeListContainerListener.clickSwipe(this);
        }
        if (mComponentSlideOver.getContentPositionX() == 0) {
            isOpenCloseLeft = true;
        } else {
            isOpenCloseLeft = false;
        }
    }

    @Override
    public void onDragStart(Component component, DragInfo dragInfo) {
    }

    @Override
    public void onDragUpdate(Component component, DragInfo dragInfo) {
        if (dragInfo.xOffset > ZERO) {
            if (isLeftRight()) {
                closeLeft(dragInfo.xOffset);
            } else {
                openRight(dragInfo.xOffset);
            }
        } else if (dragInfo.xOffset < ZERO) {
            if (isLeftRight()) {
                openLeft(dragInfo.xOffset);
            } else {
                closeRight(dragInfo.xOffset);
            }
        } else {
            HiLog.error(LABEL, "没有滑动");
        }
        offsetRatio = dragInfo.xOffset - getPaddingLeft() / (float)mComponentSlide.getEstimatedWidth();
        if (swipeListener != null) {
            swipeListener.onUpdate(this, offsetRatio, (float) dragInfo.xOffset);
        }
    }

    @Override
    public void onDragEnd(Component component, DragInfo dragInfo) {
    }

    @Override
    public void onDragCancel(Component component, DragInfo dragInfo) {
    }

    /**
     * 仅针对ListContaineriter滑动时，让上一个开启的item菜单关闭
     */
    public interface SwipeListContainerListener {
        /**
         * 开启
         *
         * @param swipeDragLayout this
         */
        void openSwipe(SwipeDragLayout swipeDragLayout);

        /**
         * 点击
         *
         * @param swipeDragLayout this
         */
        void clickSwipe(SwipeDragLayout swipeDragLayout);
    }

    /**
     * 动画
     */
    public interface SwipeListener {
        /**
         * 拖动中，可根据offset 进行其他动画
         *
         * @param layout this
         * @param offsetRatio 偏移相对于menu宽度的比例
         * @param offset 偏移量px
         */
        void onUpdate(SwipeDragLayout layout, double offsetRatio, float offset);

        /**
         * 展开完成
         *
         * @param layout this
         */
        void onOpened(SwipeDragLayout layout);

        /**
         * 关闭完成
         *
         * @param layout this
         */
        void onClosed(SwipeDragLayout layout);
    }
}
