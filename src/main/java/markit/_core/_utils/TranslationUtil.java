package markit._core._utils;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;

@Service
public class TranslationUtil {
    private final Translate translate;

    public TranslationUtil() {
        // gcloud로 인증한 기본 사용자 인증 정보를 사용해 Translate 서비스를 초기화한다.
        this.translate = TranslateOptions.getDefaultInstance().getService();
    }

    /**
     * 입력된 텍스트를 지정된 언어로 번역한다.
     * @param text 번역할 텍스트 (예: "Defining the Marketing Angle")
     * @param targetLanguage 번역할 언어 코드 (예: "ko" for Korean)
     * @return 번역된 텍스트 (예: "마케팅 방향 정의")
     */
    public String translateText(String text, String targetLanguage) {
        try {
            Translation translation = translate.translate(
                    text,
                    Translate.TranslateOption.targetLanguage(targetLanguage)
            );
            return translation.getTranslatedText();
        } catch (Exception e) {
            // 번역 실패 시 로그를 남기고 원본 텍스트를 반환
            System.err.println("번역 실패: " + e.getMessage());
            return text;
        }
    }
}
