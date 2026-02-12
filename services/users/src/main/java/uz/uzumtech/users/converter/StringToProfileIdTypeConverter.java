package uz.uzumtech.users.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.uzumtech.users.generated.dto.ProfileIdTypeDto;

@Component
public class StringToProfileIdTypeConverter implements Converter<String, ProfileIdTypeDto> {

    @Override
    public ProfileIdTypeDto convert(String source) {
        return ProfileIdTypeDto.fromValue(source);
    }
}
