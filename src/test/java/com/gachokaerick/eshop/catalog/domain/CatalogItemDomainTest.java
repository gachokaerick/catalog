package com.gachokaerick.eshop.catalog.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.catalog.exception.DomainException;
import com.gachokaerick.eshop.catalog.model.CatalogBrandDTO;
import com.gachokaerick.eshop.catalog.model.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.model.CatalogTypeDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("domain")
public class CatalogItemDomainTest {

    final String name = "jeans";
    final String description = "leather jeans";
    final BigDecimal price = BigDecimal.valueOf(90);
    final String pictureFileName = "jeans";
    final String pictureUrl = "http://www.jeans.com";
    final Integer availableStock = 20;
    final Integer restockThreshold = 10;
    final Integer maxStockThreshold = 30;
    final Boolean onReorder = false;
    final CatalogBrandDTO catalogBrand = new CatalogBrandDTO(1L, "gucci");
    final CatalogTypeDTO catalogType = new CatalogTypeDTO(1L, "clothing");

    CatalogItemDTO getDTO() {
        return new CatalogItemDTO(
            null,
            name,
            description,
            price,
            pictureFileName,
            pictureUrl,
            availableStock,
            restockThreshold,
            maxStockThreshold,
            onReorder,
            catalogBrand,
            catalogType
        );
    }

    @Test
    public void testWithNulls() {
        assertAll(
            () -> {
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(null).build();
                    }
                );
                String expected = "CatalogItemDTO cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setName(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setPrice(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setAvailableStock(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setRestockThreshold(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setMaxStockThreshold(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setCatalogBrand(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setCatalogType(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected =
                    "name, price, availableStock, restockThreshold, maxStockThreshold, catalogBrand and catalogType cannot be null";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            }
        );
    }

    @Test
    public void testWithNegatives() {
        assertAll(
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setPrice(BigDecimal.valueOf(-1));
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected = "price should be greater than zero";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setAvailableStock(-1);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected = "available stock should be greater than zero";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setRestockThreshold(-1);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected = "restock threshold should be greater than zero";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setMaxStockThreshold(-1);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                    }
                );
                String expected = "max stock threshold should be greater than zero";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            }
        );
    }
}
