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

    @Test
    public void testWithAvailableStockExceedingMaxStock() {
        CatalogItemDTO itemDTO = getDTO();
        itemDTO.setAvailableStock(10);
        itemDTO.setMaxStockThreshold(5);
        Exception exception = assertThrows(
            DomainException.class,
            () -> {
                new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
            }
        );
        String expected = "available stock should not exceed maximum stock threshold";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testWithRestockExceedingMaxStock() {
        CatalogItemDTO itemDTO = getDTO();
        itemDTO.setRestockThreshold(10);
        itemDTO.setMaxStockThreshold(5);
        Exception exception = assertThrows(
            DomainException.class,
            () -> {
                new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
            }
        );
        String expected = "restockThreshold should not exceed maximum stock threshold";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testWithNonExistingBrand() {
        CatalogItemDTO itemDTO = getDTO();
        itemDTO.setCatalogBrand(new CatalogBrandDTO(null, "brand"));
        Exception exception = assertThrows(
            DomainException.class,
            () -> {
                new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
            }
        );
        String expected = "catalog brand for this item must exist";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testWithNonExistingType() {
        CatalogItemDTO itemDTO = getDTO();
        itemDTO.setCatalogType(new CatalogTypeDTO(null, "type"));
        Exception exception = assertThrows(
            DomainException.class,
            () -> {
                new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
            }
        );
        String expected = "catalog type for this item must exist";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testStockDeduction() {
        assertAll(
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                itemDTO.setAvailableStock(0);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        CatalogItemDomain domain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                        domain.removeStock(1);
                    }
                );
                String expected = "Empty stock. Product " + itemDTO.getName() + " is sold out";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> {
                        CatalogItemDomain domain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                        domain.removeStock(-1);
                    }
                );
                String expected = "Units to remove should be greater than zero";
                String actual = exception.getMessage();
                assertTrue(actual.contains(expected));
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                CatalogItemDomain domain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                int removed = domain.removeStock(5);
                assertEquals(5, removed);
                assertEquals(15, domain.getCatalogItemDTO().getAvailableStock());
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                CatalogItemDomain domain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                int removed = domain.removeStock(25);
                assertEquals(availableStock, removed);
                assertEquals(0, domain.getCatalogItemDTO().getAvailableStock());
            }
        );
    }

    @Test
    public void testStockAddition() {
        assertAll(
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                CatalogItemDomain domain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                int added = domain.addStock(15);
                assertEquals(maxStockThreshold - availableStock, added);
                assertEquals(maxStockThreshold, domain.getCatalogItemDTO().getAvailableStock());
            },
            () -> {
                CatalogItemDTO itemDTO = getDTO();
                CatalogItemDomain domain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(itemDTO).build();
                int added = domain.addStock(5);
                assertEquals(5, added);
                assertEquals(availableStock + 5, domain.getCatalogItemDTO().getAvailableStock());
            }
        );
    }
}
