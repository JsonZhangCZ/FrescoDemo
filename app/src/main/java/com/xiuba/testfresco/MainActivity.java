package com.xiuba.testfresco;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView draweeView;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1402/12/c1/31189058_1392186616852.jpg")
                .build();
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        builder.setPressedStateOverlay(getResources().getDrawable(R.drawable.icon2));
        builder.setProgressBarImage(new ProgressBarDrawable());
//        builder.setOverlay(getResources().getDrawable(R.drawable.icon2));
        builder.setFadeDuration(3000).setRoundingParams(new RoundingParams().setRoundAsCircle(true));
        draweeView = (SimpleDraweeView)findViewById(R.id.draweeView);
        draweeView.setController(controller);
        draweeView.setHierarchy(builder.build());

        image = (ImageView)findViewById(R.id.image);
        ImageRequest request = ImageRequest.fromUri("http://img2.3lian.com/2014/f6/173/d/51.jpg");
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, this);
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
    }
}
