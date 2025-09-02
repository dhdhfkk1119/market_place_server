package com.market.market_place.item.core;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_category.ItemCategoryRepository;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.item.item_image.ItemImageRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberAddress;
import com.market.market_place.members.repositories.MemberAddressRepository;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.UserTokenHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    public ItemResponse.ItemSaveDTO save(Long id,ItemRequest.ItemSaveDTO dto) {

        Member seller = memberRepository.findById(id)
                .orElseThrow(() -> new Exception404("회원이 존재하지 않습니다"));

        ItemCategory category = itemCategoryRepository.findById(dto.getItemCategoryId())
                .orElseThrow(() -> new Exception404("카테고리를 찾을 수 없습니다"));

        MemberAddress address = memberAddressRepository.findById(dto.getMemberAddressId())
                .orElseThrow(() -> new Exception404("주소가 존재하지 않습니다."));

        Item item = dto.toEntity(category,address);
        item.setMember(seller);

        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            dto.setImageUrls(dto.getImageUrls().stream()
                    .filter(u -> u != null && !u.isBlank())
                    .distinct()
                    .toList());
            for (String url : dto.getImageUrls()) {
                item.addImage(ItemImage.of(url));
            }
        }

        Item saved = itemRepository.save(item);
        return new ItemResponse.ItemSaveDTO(saved);
    }

    public ItemResponse.ItemUpdateDTO update(Long id,Long sessionUserId,ItemRequest.ItemUpdateDTO dto) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        if (!Objects.equals(item.getMember().getId(),sessionUserId)){
            throw new Exception403("수정 권한이 없습니다.");
        }

        if (dto.getTitle() != null) {
            item.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            item.setContent(dto.getContent());
        }
        if (dto.getPrice() != null) {
            item.setPrice(dto.getPrice());
        }

        if (dto.getMemberAddressId() != null) {
            MemberAddress address = memberAddressRepository.findById(dto.getMemberAddressId())
                    .orElseThrow(() -> new Exception404("주소가 존재하지 않습니다."));
            item.setMemberAddress(address);
        }

        if (dto.getImageUrls() != null) {
            List<String> cleanedNewUrls = new ArrayList<>();
            if (dto.getImageUrls() != null) {
                for (String u : dto.getImageUrls()) {
                    if (u != null && !u.isBlank()) {
                        cleanedNewUrls.add(u);
                    }
                }
            }
            LinkedHashSet<String> distinctOrderPreserved = new LinkedHashSet<>(cleanedNewUrls);
            List<String> newUrls = new ArrayList<>(distinctOrderPreserved);

            List<ItemImage> oldImages = new ArrayList<>(item.getImages());

            Set<String> oldUrlSet = new HashSet<>();
            for (ItemImage img : oldImages) {
                oldUrlSet.add(img.getImageUrl());
            }
            Set<String> newUrlSet = new HashSet<>(newUrls);

            for (ItemImage img : oldImages) {
                if (!newUrlSet.contains(img.getImageUrl())) {
                    item.removeImage(img);
                }
            }

            for (String url : newUrls) {
                if (!oldUrlSet.contains(url)) {
                    item.addImage(ItemImage.of(url));
                }
            }
        }

        return new ItemResponse.ItemUpdateDTO(item);
    }

    public void delete(Long id, Long sessionUserId) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new Exception404("상품이 존재하지 않습니다."));

        if (!Objects.equals(item.getMember().getId(),sessionUserId)) {
            throw new Exception403("삭제 권한이 없습니다.");
        }

        itemRepository.delete(item);
    }
}
