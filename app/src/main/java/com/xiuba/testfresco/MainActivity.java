package com.xiuba.testfresco;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView draweeView;
    private ImageView image;
    private SimpleDraweeView gifView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Fresco 的按下效果代码,无论是在xml中设置或者是在代码中设置都是无效的,请知悉!
         */

        //设置Controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1402/12/c1/31189058_1392186616852.jpg")
                .build();
        //设置DraweeHierarchy
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        builder.setPressedStateOverlay(getResources().getDrawable(R.drawable.icon2));
        builder.setProgressBarImage(new ProgressBarDrawable());
//        builder.setOverlay(getResources().getDrawable(R.drawable.icon2));
        builder.setFadeDuration(3000).setRoundingParams(new RoundingParams().setRoundAsCircle(true));//设置圆角
        draweeView = (SimpleDraweeView)findViewById(R.id.draweeView);
        draweeView.setController(controller);
        draweeView.setHierarchy(builder.build());

        image = (ImageView)findViewById(R.id.image);
        //初始化ImageRequest
        ImageRequest request = ImageRequest.fromUri("http://img2.3lian.com/2014/f6/173/d/51.jpg");
        //从Fresco中拿到一个ImagePipline
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        //初始化DataSource
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, this);
        //订阅事件来从中获得网络下载的Bitmap
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     // 你可以直接在这里使用Bitmap，没有别的限制要求，也不需要回收
                                     image.setImageBitmap(bitmap);
                                 }
                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             }, UiThreadImmediateExecutorService.getInstance());
        //从Fresco中获取已下载的文件
//        File file = ((FileBinaryResource)Fresco.getImagePipelineFactory().getMainFileCache().getResource(DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(avatar),null))).getFile();
        //得到Fresco的缓存
//        long frescoCache = Fresco.getImagePipelineFactory().getMainFileCache().getSize();
        //清空Fresco中的缓存
//        Fresco.getImagePipelineFactory().getMainFileCache().clearAll();



        //GIF 相关
        gifView = (SimpleDraweeView) findViewById(R.id.gifView);
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (animatable!=null){
                    animatable.start();
                }
            }
        };
        DraweeController controller11 = Fresco.newDraweeControllerBuilder()
                //设置URI
                .setUri("http://img4.imgtn.bdimg.com/it/u=388217056,473241222&fm=21&gp=0.jpg")
                .setAutoPlayAnimations(false)//设置加载完后自动播放
                .setControllerListener(controllerListener)//设置监听器
                .build();//设置自动播放
        gifView.setController(controller11);

    }
}
