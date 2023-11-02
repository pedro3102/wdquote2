import { useMenuStore } from '@/store/modules/menu/menu-store'
import { IMenuItem } from '@/store/modules/menu/menu-store-model'
import { ACTIONS } from '@/shared/const/auth-constants'
import { vendorRouteName } from '@/views/home/mod/inventory/vendor/vendor-entity'
import { buildPath } from '@/shared/config/router/router-utils'
import { locationRouteName } from '@/views/home/mod/inventory/location/location-entity'
import { languageRouteName } from '@/views/home/mod/inventory/language/language-entity'
import { productCategoryRouteName } from '@/views/home/mod/inventory/product-category/product-category-entity'
import { systemModelRouteName } from '@/views/home/mod/inventory/system-model/system-model-entity'
import { unitOfMeasureRouteName } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-entity'
import { systemRouteName } from '@/views/home/mod/inventory/system/system-entity'
import { deliveryZoneRouteName } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-entity'
import { taxAreaCodeRouteName } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-entity'
import { customerRouteName } from '@/views/home/mod/inventory/customer/customer-entity'
import { customerContactRouteName } from '@/views/home/mod/inventory/customer-contact/customer-contact-entity'

const rightIconClass = 'fa-angle-left'
const displayNone = 'display: none;'

const menuStore = useMenuStore()

const globalCatalogMenu = (): IMenuItem => ({
  id: 2, // 'catalogs',
  open: false,
  label: 'mainMenu.global-catalogs.menu',
  icon: 'fa fa-book',
  rightIcon: rightIconClass,
  style: displayNone,
  to: '',
  access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG),
  items: [
    {
      id: languageRouteName,
      label: 'mainMenu.global-catalogs.language',
      to: buildPath(languageRouteName),
      icon: 'language',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_LANGUAGE),
      items: [],
    },
    {
      id: vendorRouteName,
      label: 'mainMenu.global-catalogs.vendor',
      to: buildPath(vendorRouteName),
      icon: 'store',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_VENDOR),
      items: [],
    },
    {
      id: locationRouteName,
      label: 'mainMenu.global-catalogs.location',
      to: buildPath(locationRouteName),
      icon: 'location',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_LOCATION),
      items: [],
    },
    {
      id: taxAreaCodeRouteName,
      label: 'mainMenu.global-catalogs.taxAreaCode',
      to: buildPath(taxAreaCodeRouteName),
      icon: 'indent',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_TAX_AREA_CODE),
      items: [],
    },
    {
      id: deliveryZoneRouteName,
      label: 'mainMenu.global-catalogs.deliveryZone',
      to: buildPath(deliveryZoneRouteName),
      icon: 'map',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_DELIVERY_ZONE),
      items: [],
    },
    {
      id: unitOfMeasureRouteName,
      label: 'mainMenu.global-catalogs.unitOfMeasure',
      to: buildPath(unitOfMeasureRouteName),
      icon: 'ruler',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_UNIT_OF_MEASURE),
      items: [],
    },
    {
      id: productCategoryRouteName,
      label: 'mainMenu.global-catalogs.productCategory',
      to: buildPath(productCategoryRouteName),
      icon: 'list',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_PRODUCT_CATEGORY),
      items: [],
    },
    {
      id: systemRouteName,
      label: 'mainMenu.global-catalogs.system',
      to: buildPath(systemRouteName),
      icon: 'door-open',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_SYSTEM),
      items: [],
    },
    {
      id: systemModelRouteName,
      label: 'mainMenu.global-catalogs.systemModel',
      to: buildPath(systemModelRouteName),
      icon: 'border-all',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CATALOG_SYSTEM_MODEL),
      items: [],
    },
    {
      id: customerRouteName,
      label: 'mainMenu.global-catalogs.customer',
      to: buildPath(customerRouteName),
      icon: 'users-between-lines',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CUSTOMER),
      items: [],
    },
    {
      id: customerContactRouteName,
      label: 'mainMenu.global-catalogs.customerContact',
      to: buildPath(customerContactRouteName),
      icon: 'id-card',
      access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_CUSTOMER_CONTACT),
      items: [],
    },
  ],
})

export default globalCatalogMenu
