/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.domain.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AppTest {
    private App app;

    @Before
    public void setup() {
        app = new App("test");
        app.setAppSearchParameter(new AppSearchParameter().locales("es".split(",")));
    }

    @Test
    public void getSearchName() {
        app.setSearchName(":en: A Photo Manager | :es: A Photo Manager (Manejador de fotos) | :nl: A Photo Manager (Een Foto Beheerder) | :pt: Um Gerenciador de Fotos (A Photo Manager) | :ru: Фото менеджер (A Photo Manager) | :uk: Менеджер фотографій (A Photo Manager) | :zh-CN: A Photo Manager (照片管理器)");
        assertEquals("A Photo Manager (Manejador de fotos)", app.getLocalizedName());
    }

    @Test
    public void getSearchNameDefault() {
        app.setSearchName("A Photo Manager");
        assertEquals("A Photo Manager", app.getLocalizedName());
    }

    @Test
    public void getLocalizedSummary() {
        app.setSearchSummary(":de: Verwalte lokale Photos: Suchen/Kopieren/Exif bearbeiten/Gallerie/Landkarte.\n:en: Manage local photos: Find/Copy/Edit-Exif and show in Gallery or Map.\n:ar: مدير للصور المحلية يقوم بـ: بحث ونسخ وتعديل الصور ووضعها في معرض صور او في خريطة\n:es: Gestiona fotos locales: Encontrar/Copiar/Editar-Exif y mostrar en Galería o Mapa\n:fr: Gérez vos photos locales : Trouvez / Copiez / Éditez les Exif et affichez vos ph\n:id: Mengelola foto lokal: menemukan/Copy/Edit-Exif dan Tampilkan di galeri atau peta\n:ja: ローカルの写真を管理: Exifの検索/コピー/編集、ギャラリーや地図に表示します。\n:nl: Lokale foto's beheren: vinden/kopieren/Exif bewerken en weergeven in galerij of \n:pt: Gerenciar fotos locais: Encontrar/Copiar/Editar e mostrar na Galeria ou Mapa.\n:ru: Управление локальными фотографиями: Find/Edit-Exif и показать в Галерее или Карт\n:uk: Керування локальними фотографіями: Знайти/Скопіювати/Змінити-Exif і показати в Г\n:zh-CN: 管理本地照片：查找/复制/编辑EXIF信息并在图库或地图上展示照片.\n:zh-TW: 管理本地照片：查找/復制/編輯EXIF信息並在圖庫或地圖上展示照片.");
        assertEquals("Gestiona fotos locales: Encontrar/Copiar/Editar-Exif y mostrar en Galería o Mapa", app.getLocalizedSummary());
    }
}
