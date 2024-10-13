import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { NavbarComponent } from './_components/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { MaterialType } from '../../core/enums/material-type.enum';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';  // Importez OrbitControls

@Component({
  selector: 'app-customization',
  standalone: true,
  imports: [NavbarComponent, CommonModule],
  templateUrl: './customization.component.html',
  styleUrls: ['./customization.component.scss']
})
export class CustomizationComponent implements AfterViewInit {
  materialTypes = Object.values(MaterialType);

  @ViewChild('takchitaCanvas', { static: true }) takchitaCanvas!: ElementRef<HTMLCanvasElement>;
  controls!: OrbitControls;  // OrbitControls pour déplacer et zoomer
  camera!: THREE.PerspectiveCamera;

  getMaterialIcon(material: MaterialType): string {
    switch (material) {
      case MaterialType.FABRIC:
        return 'pi pi-image';
      case MaterialType.LACE:
        return 'pi pi-ticket';
      case MaterialType.BUTTON:
        return 'pi pi-circle-on';
      case MaterialType.ACCESSORY:
        return 'pi pi-briefcase';
      case MaterialType.THREAD:
        return 'pi pi-slack';
      case MaterialType.RIBBON:
        return 'pi pi-link';
      case MaterialType.DECORATION:
        return 'pi pi-star';
      default:
        return 'pi pi-question';
    }
  }

  ngAfterViewInit() {
    this.initialize3DModel();
  }

  initialize3DModel() {
    // Création de la scène et configuration de la caméra
    const scene = new THREE.Scene();
    this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    this.camera.position.z = 2;

    // Création du renderer
    const renderer = new THREE.WebGLRenderer({
      canvas: this.takchitaCanvas.nativeElement,
      antialias: true
    });
    renderer.setSize(window.innerWidth, window.innerHeight);

    // Lumière
    const light = new THREE.DirectionalLight(0xffffff, 2);
    light.position.set(2, 2, 2).normalize();
    scene.add(light);

    // Charger le modèle Moroccan Takchita
    const loader = new GLTFLoader();
    loader.load('assets/moroccan.glb', (gltf: any) => {
      const model = gltf.scene;
      model.scale.set(2, 2, 2);  // Ajustez la taille du modèle
      scene.add(model);
    });

    // Initialiser les contrôles d'orbite (mouvement et zoom)
    this.controls = new OrbitControls(this.camera, renderer.domElement);
    this.controls.enableDamping = true; // Douceur lors du mouvement
    this.controls.dampingFactor = 0.05;
    this.controls.screenSpacePanning = false; // Ne pas permettre de "faire glisser" le modèle
    this.controls.maxPolarAngle = Math.PI / 2; // Limiter la rotation verticale

    // Fonction d'animation
    const animate = () => {
      requestAnimationFrame(animate);
      this.controls.update();  // Mettre à jour OrbitControls
      renderer.render(scene, this.camera);
    };

    // Démarrer l'animation
    animate();
  }

  // Fonction pour zoomer avant
  zoomIn() {
    this.camera.position.z -= 0.5;  // Rapprocher la caméra
  }

  // Fonction pour zoomer arrière
  zoomOut() {
    this.camera.position.z += 0.5;  // Éloigner la caméra
  }
}
